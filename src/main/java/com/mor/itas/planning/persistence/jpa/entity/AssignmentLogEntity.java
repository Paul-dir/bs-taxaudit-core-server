package com.mor.itas.planning.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assignment_log")
@Getter
@Setter
public class AssignmentLogEntity {
    @Id
    private UUID id;
    private UUID auditCaseId;
    private UUID fromUserId;
    private UUID toUserId;
    private Instant transitionDate;
    private String reason;
    private String statusTransition;
}
