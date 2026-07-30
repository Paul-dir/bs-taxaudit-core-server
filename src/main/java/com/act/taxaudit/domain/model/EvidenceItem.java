package com.act.taxaudit.domain.model;

import com.act.taxaudit.domain.valueobject.EvidenceSourceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing evidence collected during desk audit.
 * This is a child entity within the DeskAudit aggregate, not a separate aggregate.
 */
public class EvidenceItem {
    private final String evidenceId;
    private final EvidenceSourceType sourceType;
    private final String description;
    private final String documentReference;  // DMS reference, nullable
    private final LocalDateTime collectedAt;
    private final String collectedByActorId;

    @JsonCreator
    public EvidenceItem(
            @JsonProperty("evidenceId") String evidenceId,
            @JsonProperty("sourceType") EvidenceSourceType sourceType,
            @JsonProperty("description") String description,
            @JsonProperty("documentReference") String documentReference,
            @JsonProperty("collectedAt") LocalDateTime collectedAt,
            @JsonProperty("collectedByActorId") String collectedByActorId) {
        if (evidenceId == null || evidenceId.isBlank()) {
            throw new IllegalArgumentException("Evidence ID cannot be null or blank");
        }
        if (sourceType == null) {
            throw new IllegalArgumentException("Source type cannot be null");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (collectedAt == null) {
            throw new IllegalArgumentException("Collected at cannot be null");
        }
        if (collectedByActorId == null || collectedByActorId.isBlank()) {
            throw new IllegalArgumentException("Collected by actor ID cannot be null or blank");
        }

        this.evidenceId = evidenceId;
        this.sourceType = sourceType;
        this.description = description;
        this.documentReference = documentReference;
        this.collectedAt = collectedAt;
        this.collectedByActorId = collectedByActorId;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public EvidenceSourceType getSourceType() {
        return sourceType;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public String getCollectedByActorId() {
        return collectedByActorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceItem that = (EvidenceItem) o;
        return evidenceId.equals(that.evidenceId) &&
               sourceType == that.sourceType &&
               description.equals(that.description) &&
               Objects.equals(documentReference, that.documentReference) &&
               collectedAt.equals(that.collectedAt) &&
               collectedByActorId.equals(that.collectedByActorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId, sourceType, description, documentReference, collectedAt, collectedByActorId);
    }
}