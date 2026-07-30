package com.act.taxaudit.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value object representing a document request to the taxpayer.
 */
public class DocumentRequest {
    private final String requestId;
    private final String documentType;
    private final String description;
    private final LocalDateTime requestedAt;
    private final String requestedByActorId;
    private final LocalDateTime dueDate;
    private final int reminderCount;

    @JsonCreator
    public DocumentRequest(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("description") String description,
            @JsonProperty("requestedAt") LocalDateTime requestedAt,
            @JsonProperty("requestedByActorId") String requestedByActorId,
            @JsonProperty("dueDate") LocalDateTime dueDate,
            @JsonProperty("reminderCount") int reminderCount) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be null or blank");
        }
        if (documentType == null || documentType.isBlank()) {
            throw new IllegalArgumentException("Document type cannot be null or blank");
        }
        if (requestedAt == null) {
            throw new IllegalArgumentException("Requested at cannot be null");
        }
        if (requestedByActorId == null || requestedByActorId.isBlank()) {
            throw new IllegalArgumentException("Requested by actor ID cannot be null or blank");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (reminderCount < 0) {
            throw new IllegalArgumentException("Reminder count cannot be negative");
        }

        this.requestId = requestId;
        this.documentType = documentType;
        this.description = description;
        this.requestedAt = requestedAt;
        this.requestedByActorId = requestedByActorId;
        this.dueDate = dueDate;
        this.reminderCount = reminderCount;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getRequestedByActorId() {
        return requestedByActorId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public int getReminderCount() {
        return reminderCount;
    }

    public DocumentRequest withReminderCount(int reminderCount) {
        return new DocumentRequest(this.requestId, this.documentType, this.description,
                                   this.requestedAt, this.requestedByActorId,
                                   this.dueDate, reminderCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentRequest that = (DocumentRequest) o;
        return reminderCount == that.reminderCount &&
               requestId.equals(that.requestId) &&
               documentType.equals(that.documentType) &&
               description.equals(that.description) &&
               requestedAt.equals(that.requestedAt) &&
               requestedByActorId.equals(that.requestedByActorId) &&
               dueDate.equals(that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, documentType, description, requestedAt, requestedByActorId, dueDate, reminderCount);
    }
}