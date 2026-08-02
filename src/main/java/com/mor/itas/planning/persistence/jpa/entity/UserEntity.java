package com.mor.itas.planning.persistence.jpa.entity;

import com.itas.bs.taxaudit.domain.valueobject.AuditType;
import com.itas.bs.taxaudit.domain.valueobject.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    private UUID id;
    private String fullName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String status;

    private String assignedRegion;
    private String assignedTaxCenter;
    
    @Enumerated(EnumType.STRING)
    private AuditType auditType;
    
    private String teamId;
    
    private int currentWorkload;
    private int maxCapacity;
}
