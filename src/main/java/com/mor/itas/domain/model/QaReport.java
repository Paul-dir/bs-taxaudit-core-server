package com.mor.itas.domain.model;

import com.mor.itas.domain.aggregate.AggregateRoot;
import com.mor.itas.domain.exception.DomainException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * QA Report aggregate root.
 * Represents the formal QA report generated after review findings are recorded.
 */
public class QaReport extends AggregateRoot {

    private final UUID id;
    private final UUID reviewCaseId;
    private String title;
    private String content;
    private String summary;
    private final Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private QaReport(UUID id, UUID reviewCaseId, String title, String content) {
        this.id = id;
        this.reviewCaseId = reviewCaseId;
        this.title = title;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static QaReport generate(UUID reviewCaseId, String title, String content, String summary) {
        Objects.requireNonNull(reviewCaseId, "reviewCaseId");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(content, "content");

        QaReport report = new QaReport(UUID.randomUUID(), reviewCaseId, title, content);
        report.summary = summary;
        return report;
    }

    public void updateContent(String content, String summary) {
        if (content == null || content.isBlank()) {
            throw new DomainException("content cannot be empty");
        }
        this.content = content;
        this.summary = summary;
        touch();
    }

    private void touch() { this.updatedAt = Instant.now(); }

    @Override
    public UUID getId() { return id; }
    public UUID getReviewCaseId() { return reviewCaseId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSummary() { return summary; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    public static QaReport rehydrate(UUID id, UUID reviewCaseId, String title,
                                      String content, String summary,
                                      Instant createdAt, Instant updatedAt, Long version) {
        QaReport report = new QaReport(id, reviewCaseId, title, content);
        report.summary = summary;
        report.updatedAt = updatedAt;
        report.version = version;
        report.pullEvents();
        return report;
    }
}