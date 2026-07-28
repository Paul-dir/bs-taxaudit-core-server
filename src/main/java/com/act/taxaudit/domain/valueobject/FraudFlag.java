package com.act.taxaudit.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value object representing a fraud indicator flag.
 */
public class FraudFlag {
    private final String flagId;
    private final String indicatorNotes;
    private final LocalDateTime flaggedAt;
    private final String flaggedByActorId;

    public FraudFlag(String flagId, String indicatorNotes, LocalDateTime flaggedAt, String flaggedByActorId) {
        if (flagId == null || flagId.isBlank()) {
            throw new IllegalArgumentException("Flag ID cannot be null or blank");
        }
        if (indicatorNotes == null || indicatorNotes.isBlank()) {
            throw new IllegalArgumentException("Indicator notes cannot be null or blank");
        }
        if (flaggedAt == null) {
            throw new IllegalArgumentException("Flagged at cannot be null");
        }
        if (flaggedByActorId == null || flaggedByActorId.isBlank()) {
            throw new IllegalArgumentException("Flagged by actor ID cannot be null or blank");
        }

        this.flagId = flagId;
        this.indicatorNotes = indicatorNotes;
        this.flaggedAt = flaggedAt;
        this.flaggedByActorId = flaggedByActorId;
    }

    public String getFlagId() {
        return flagId;
    }

    public String getIndicatorNotes() {
        return indicatorNotes;
    }

    public LocalDateTime getFlaggedAt() {
        return flaggedAt;
    }

    public String getFlaggedByActorId() {
        return flaggedByActorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FraudFlag fraudFlag = (FraudFlag) o;
        return flagId.equals(fraudFlag.flagId) &&
               indicatorNotes.equals(fraudFlag.indicatorNotes) &&
               flaggedAt.equals(fraudFlag.flaggedAt) &&
               flaggedByActorId.equals(fraudFlag.flaggedByActorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flagId, indicatorNotes, flaggedAt, flaggedByActorId);
    }
}