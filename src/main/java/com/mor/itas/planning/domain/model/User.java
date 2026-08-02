package com.mor.itas.planning.domain.model;

import com.itas.bs.taxaudit.domain.aggregate.AggregateRoot;
import com.itas.bs.taxaudit.domain.valueobject.OrgContext;
import com.itas.bs.taxaudit.domain.valueobject.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@ToString
public class User implements AggregateRoot {
    private final UUID id;
    private final String fullName;
    private final String email;
    private final Role role;
    private final String status;
    private final OrgContext orgContext;
    private int currentWorkload;
    private final int maxCapacity;

    public void incrementWorkload() {
        if (this.currentWorkload >= this.maxCapacity) {
            throw new IllegalStateException("User has reached maximum capacity: " + maxCapacity);
        }
        this.currentWorkload++;
    }
}
