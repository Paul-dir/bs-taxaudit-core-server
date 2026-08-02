package com.mor.itas.planning.persistence.jpa.entity;

import com.itas.bs.taxaudit.domain.valueobject.AuditPlanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "annual_audit_plan")
@Getter
@Setter
public class AnnualAuditPlanEntity {
    @Id
    private UUID id;
    
    private int planYear;
    
    @Enumerated(EnumType.STRING)
    private AuditPlanStatus status;
    
    private int totalCasesPlanned;
    private int totalCasesCreated;
    
    private UUID directorId;
    
    // Simplification for scaffolding: versions and plannedVolumeByType can be mapped as JSON or child tables
}
