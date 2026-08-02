package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.api.dto.request.DistributeCasesRequest;
import com.itas.bs.taxaudit.application.port.AssignmentLogRepositoryPort;
import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.application.port.UserRepositoryPort;
import com.itas.bs.taxaudit.application.port.in.CaseAssignmentUseCase;
import com.itas.bs.taxaudit.domain.model.AssignmentLog;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import com.itas.bs.taxaudit.domain.model.User;
import com.itas.bs.taxaudit.domain.valueobject.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseAssignmentService implements CaseAssignmentUseCase {
    private final AuditCaseRepositoryPort auditCaseRepository;
    private final UserRepositoryPort userRepository;
    private final AssignmentLogRepositoryPort assignmentLogRepository;

    @Override
    @Transactional
    public List<AuditCase> prioritizeAndStoreCases(List<UUID> caseIds, UUID managerId) {
        List<AuditCase> cases = auditCaseRepository.findAllById(caseIds);
        List<AssignmentLog> logs = new ArrayList<>();

        for (AuditCase auditCase : cases) {
            auditCase.prioritizeAndStore();
            logs.add(createLog(auditCase.getId(), managerId, null, "Manager prioritized and stored case", auditCase.getStatus().name()));
        }

        auditCaseRepository.saveAll(cases);
        assignmentLogRepository.saveAll(logs);
        return cases;
    }

    @Override
    @Transactional
    public List<AuditCase> distributeToTeamLeaders(DistributeCasesRequest request, UUID managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
        
        List<AuditCase> cases = auditCaseRepository.findAllById(request.getCaseIds());
        List<AssignmentLog> logs = new ArrayList<>();
        List<User> modifiedTeamLeaders = new ArrayList<>();

        if ("MANUAL".equalsIgnoreCase(request.getMethod())) {
            User targetTL = userRepository.findById(request.getTargetUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Target Team Leader not found"));
            
            for (AuditCase c : cases) {
                c.assignToTeamLeader(targetTL.getId());
                targetTL.incrementWorkload();
                logs.add(createLog(c.getId(), managerId, targetTL.getId(), "Manual assignment to TL", c.getStatus().name()));
            }
            modifiedTeamLeaders.add(targetTL);
        } else {
            // AUTO Distribution
            // Group by audit type
            Map<com.itas.bs.taxaudit.domain.valueobject.AuditType, List<AuditCase>> casesByType = cases.stream()
                    .collect(Collectors.groupingBy(AuditCase::getAuditType));
            
            for (Map.Entry<com.itas.bs.taxaudit.domain.valueobject.AuditType, List<AuditCase>> entry : casesByType.entrySet()) {
                List<User> teamLeaders = userRepository.findByRoleAndTaxCenterAndAuditType(
                        Role.TEAM_LEADER, manager.getOrgContext().getAssignedTaxCenter(), entry.getKey());
                
                if (teamLeaders.isEmpty()) {
                    throw new IllegalStateException("No available team leaders for audit type: " + entry.getKey());
                }

                List<AuditCase> sortedCases = entry.getValue().stream()
                        .sorted((c1, c2) -> Double.compare(
                                c2.getRiskScore() != null ? c2.getRiskScore() : 0.0, 
                                c1.getRiskScore() != null ? c1.getRiskScore() : 0.0))
                        .collect(Collectors.toList());

                // Round robin distribution
                int tlIndex = 0;
                for (AuditCase c : sortedCases) {
                    User tl = teamLeaders.get(tlIndex % teamLeaders.size());
                    c.assignToTeamLeader(tl.getId());
                    tl.incrementWorkload();
                    logs.add(createLog(c.getId(), managerId, tl.getId(), "Auto assignment to TL", c.getStatus().name()));
                    if (!modifiedTeamLeaders.contains(tl)) modifiedTeamLeaders.add(tl);
                    tlIndex++;
                }
            }
        }

        auditCaseRepository.saveAll(cases);
        userRepository.saveAll(modifiedTeamLeaders);
        assignmentLogRepository.saveAll(logs);
        return cases;
    }

    @Override
    @Transactional
    public List<AuditCase> distributeToAuditors(DistributeCasesRequest request, UUID teamLeaderId) {
        User teamLeader = userRepository.findById(teamLeaderId)
                .orElseThrow(() -> new IllegalArgumentException("Team Leader not found"));
        
        List<AuditCase> cases = auditCaseRepository.findAllById(request.getCaseIds());
        
        for (AuditCase c : cases) {
            if (!teamLeaderId.equals(c.getTeamLeaderId())) {
                throw new IllegalStateException("Team leader can only assign their own cases.");
            }
        }

        List<AssignmentLog> logs = new ArrayList<>();
        List<User> modifiedAuditors = new ArrayList<>();

        if ("MANUAL".equalsIgnoreCase(request.getMethod())) {
            User targetAuditor = userRepository.findById(request.getTargetUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Target Auditor not found"));
            
            if (!teamLeader.getOrgContext().getTeamId().equals(targetAuditor.getOrgContext().getTeamId())) {
                throw new IllegalStateException("Target auditor must belong to the same team.");
            }

            for (AuditCase c : cases) {
                c.assignToAuditor(targetAuditor.getId());
                targetAuditor.incrementWorkload();
                logs.add(createLog(c.getId(), teamLeaderId, targetAuditor.getId(), "Manual assignment to Auditor", c.getStatus().name()));
            }
            modifiedAuditors.add(targetAuditor);
        } else {
            // AUTO Distribution
            List<User> teamAuditors = userRepository.findByTeamId(teamLeader.getOrgContext().getTeamId());
            if (teamAuditors.isEmpty()) {
                throw new IllegalStateException("No available auditors in team.");
            }

            List<AuditCase> sortedCases = cases.stream()
                    .sorted((c1, c2) -> Double.compare(
                            c2.getRiskScore() != null ? c2.getRiskScore() : 0.0, 
                            c1.getRiskScore() != null ? c1.getRiskScore() : 0.0))
                    .collect(Collectors.toList());

            int auditorIndex = 0;
            for (AuditCase c : sortedCases) {
                User auditor = teamAuditors.get(auditorIndex % teamAuditors.size());
                c.assignToAuditor(auditor.getId());
                auditor.incrementWorkload();
                logs.add(createLog(c.getId(), teamLeaderId, auditor.getId(), "Auto assignment to Auditor", c.getStatus().name()));
                if (!modifiedAuditors.contains(auditor)) modifiedAuditors.add(auditor);
                auditorIndex++;
            }
        }

        auditCaseRepository.saveAll(cases);
        userRepository.saveAll(modifiedAuditors);
        assignmentLogRepository.saveAll(logs);
        return cases;
    }

    private AssignmentLog createLog(UUID caseId, UUID from, UUID to, String reason, String status) {
        return AssignmentLog.builder()
                .id(UUID.randomUUID())
                .auditCaseId(caseId)
                .fromUserId(from)
                .toUserId(to)
                .reason(reason)
                .statusTransition(status)
                .transitionDate(Instant.now())
                .build();
    }
}
