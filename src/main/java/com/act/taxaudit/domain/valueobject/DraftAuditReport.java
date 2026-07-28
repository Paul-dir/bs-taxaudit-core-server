package com.act.taxaudit.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value object representing a draft audit report.
 */
public class DraftAuditReport {
    private final String narrative;
    private final String findingsSummary;
    private final LocalDateTime preparedAt;
    private final String preparedByActorId;

    public DraftAuditReport(String narrative, String findingsSummary, LocalDateTime preparedAt, String preparedByActorId) {
        if (narrative == null || narrative.isBlank()) {
            throw new IllegalArgumentException("Report narrative cannot be null or blank");
        }
        if (findingsSummary == null || findingsSummary.isBlank()) {
            throw new IllegalArgumentException("Findings summary cannot be null or blank");
        }
        if (preparedAt == null) {
            throw new IllegalArgumentException("Prepared at cannot be null");
        }
        if (preparedByActorId == null || preparedByActorId.isBlank()) {
            throw new IllegalArgumentException("Prepared by actor ID cannot be null or blank");
        }

        this.narrative = narrative;
        this.findingsSummary = findingsSummary;
        this.preparedAt = preparedAt;
        this.preparedByActorId = preparedByActorId;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getFindingsSummary() {
        return findingsSummary;
    }

    public LocalDateTime getPreparedAt() {
        return preparedAt;
    }

    public String getPreparedByActorId() {
        return preparedByActorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DraftAuditReport that = (DraftAuditReport) o;
        return narrative.equals(that.narrative) &&
               findingsSummary.equals(that.findingsSummary) &&
               preparedAt.equals(that.preparedAt) &&
               preparedByActorId.equals(that.preparedByActorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(narrative, findingsSummary, preparedAt, preparedByActorId);
    }
}