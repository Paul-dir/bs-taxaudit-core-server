package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.ExitConference;
import com.mor.itas.persistence.jpa.entity.ExitConferenceEntity;
import com.mor.itas.persistence.jpa.repository.ExitConferenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExitConferenceAdapter {
    private final ExitConferenceRepository repository;

    public ExitConferenceAdapter(ExitConferenceRepository repository) {
        this.repository = repository;
    }

    public ExitConference save(ExitConference conference) {
        ExitConferenceEntity entity = toEntity(conference);
        ExitConferenceEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public Optional<ExitConference> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .findFirst()
                .map(this::toDomain);
    }

    private ExitConferenceEntity toEntity(ExitConference conference) {
        ExitConferenceEntity entity = new ExitConferenceEntity();
        entity.setId(conference.getId());
        entity.setQaReviewCaseId(conference.getQaReviewCaseId());
        entity.setScheduledAt(conference.getScheduledAt());
        entity.setAttendees(conference.getAttendees());
        entity.setMinutesOutcomeNotes(conference.getMinutesOutcomeNotes());
        entity.setConductedAt(conference.getConductedAt());
        return entity;
    }

    private ExitConference toDomain(ExitConferenceEntity entity) {
        ExitConference conference = new ExitConference();
        conference.setId(entity.getId());
        conference.setQaReviewCaseId(entity.getQaReviewCaseId());
        conference.setScheduledAt(entity.getScheduledAt());
        conference.setAttendees(entity.getAttendees());
        conference.setMinutesOutcomeNotes(entity.getMinutesOutcomeNotes());
        conference.setConductedAt(entity.getConductedAt());
        conference.setCreatedAt(entity.getCreatedAt());
        conference.setUpdatedAt(entity.getUpdatedAt());
        return conference;
    }
}