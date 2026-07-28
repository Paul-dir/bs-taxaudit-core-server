package com.act.taxaudit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String TOPIC_DOCUMENTS_REQUESTED = "taxaudit.deskaudit.documents-requested.v1";
    public static final String TOPIC_REMINDER = "taxaudit.deskaudit.reminder.v1";
    public static final String TOPIC_DRAFT_REPORT_READY = "taxaudit.deskaudit.draft-report-ready.v1";
    public static final String TOPIC_RISK_PROFILE_UPDATE = "taxaudit.deskaudit.risk-profile-update.v1";
    public static final String TOPIC_CASE_STATUS_CHANGED = "taxaudit.deskaudit.case-status-changed.v1";
    public static final String TOPIC_FRAUD_FLAGGED = "taxaudit.deskaudit.fraud-flagged.v1";

    @Bean
    public NewTopic documentsRequestedTopic() {
        return TopicBuilder.name(TOPIC_DOCUMENTS_REQUESTED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic documentsRequestedDltTopic() {
        return TopicBuilder.name(TOPIC_DOCUMENTS_REQUESTED + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reminderTopic() {
        return TopicBuilder.name(TOPIC_REMINDER)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reminderDltTopic() {
        return TopicBuilder.name(TOPIC_REMINDER + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic draftReportReadyTopic() {
        return TopicBuilder.name(TOPIC_DRAFT_REPORT_READY)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic draftReportReadyDltTopic() {
        return TopicBuilder.name(TOPIC_DRAFT_REPORT_READY + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic riskProfileUpdateTopic() {
        return TopicBuilder.name(TOPIC_RISK_PROFILE_UPDATE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic riskProfileUpdateDltTopic() {
        return TopicBuilder.name(TOPIC_RISK_PROFILE_UPDATE + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic caseStatusChangedTopic() {
        return TopicBuilder.name(TOPIC_CASE_STATUS_CHANGED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic caseStatusChangedDltTopic() {
        return TopicBuilder.name(TOPIC_CASE_STATUS_CHANGED + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic fraudFlaggedTopic() {
        return TopicBuilder.name(TOPIC_FRAUD_FLAGGED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic fraudFlaggedDltTopic() {
        return TopicBuilder.name(TOPIC_FRAUD_FLAGGED + ".DLT")
                .partitions(3)
                .replicas(1)
                .build();
    }
}