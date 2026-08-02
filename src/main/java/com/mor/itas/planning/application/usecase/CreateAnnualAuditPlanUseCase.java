package com.mor.itas.planning.application.usecase;

import com.itas.bs.taxaudit.application.port.AnnualAuditPlanRepositoryPort;
import com.itas.bs.taxaudit.application.port.EventDispatchPort;
import com.itas.bs.taxaudit.domain.event.AnnualAuditPlanCreatedEvent;
import com.itas.bs.taxaudit.domain.model.AnnualAuditPlan;
import com.itas.bs.taxaudit.domain.valueobject.AuditPlanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAnnualAuditPlanUseCase {
    private final AnnualAuditPlanRepositoryPort planRepository;
    private final EventDispatchPort eventDispatchPort;

    @Transactional
    public UUID execute(int year, int totalCasesPlanned, UUID directorId) {
        AnnualAuditPlan plan = AnnualAuditPlan.builder()
                .id(UUID.randomUUID())
                .year(year)
                .status(AuditPlanStatus.DRAFT)
                .totalCasesPlanned(totalCasesPlanned)
                .totalCasesCreated(0)
                .directorId(directorId)
                .versions(new ArrayList<>())
                .plannedVolumeByType(new HashMap<>())
                .build();

        AnnualAuditPlan savedPlan = planRepository.save(plan);

        AnnualAuditPlanCreatedEvent event = new AnnualAuditPlanCreatedEvent(
                savedPlan.getId(), year, totalCasesPlanned, directorId
        );
        eventDispatchPort.dispatch(event);

        return savedPlan.getId();
    }
}
