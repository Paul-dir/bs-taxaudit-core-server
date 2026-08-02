package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AuditCaseRepositoryPort;
import com.itas.bs.taxaudit.domain.model.AuditCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAuditCasesByAuditorUseCase {
    private final AuditCaseRepositoryPort caseRepository;

    public List<AuditCase> getByAuditorId(UUID auditorId) {
        return caseRepository.findAll().stream()
                .filter(c -> auditorId.equals(c.getAssignedAuditorId()))
                .collect(Collectors.toList());
    }
}
