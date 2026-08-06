package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.ExitConferenceAgenda;
import com.mor.itas.persistence.jpa.entity.ExitConferenceAgendaEntity;
import com.mor.itas.persistence.jpa.repository.ExitConferenceAgendaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExitConferenceAgendaAdapter {
    private final ExitConferenceAgendaRepository repository;

    public ExitConferenceAgendaAdapter(ExitConferenceAgendaRepository repository) {
        this.repository = repository;
    }

    public ExitConferenceAgenda save(ExitConferenceAgenda agenda) {
        ExitConferenceAgendaEntity entity = toEntity(agenda);
        ExitConferenceAgendaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public List<ExitConferenceAgenda> findByQaReviewCaseId(Long qaReviewCaseId) {
        return repository.findAll().stream()
                .filter(e -> e.getQaReviewCaseId().equals(qaReviewCaseId))
                .map(this::toDomain)
                .toList();
    }

    private ExitConferenceAgendaEntity toEntity(ExitConferenceAgenda agenda) {
        ExitConferenceAgendaEntity entity = new ExitConferenceAgendaEntity();
        entity.setId(agenda.getId());
        entity.setQaReviewCaseId(agenda.getQaReviewCaseId());
        entity.setPreparedByRole(agenda.getPreparedByRole());
        entity.setItems(agenda.getItems());
        entity.setApprovalRequestId(agenda.getApprovalRequestId());
        entity.setStatus(agenda.getStatus());
        return entity;
    }

    private ExitConferenceAgenda toDomain(ExitConferenceAgendaEntity entity) {
        ExitConferenceAgenda agenda = new ExitConferenceAgenda();
        agenda.setId(entity.getId());
        agenda.setQaReviewCaseId(entity.getQaReviewCaseId());
        agenda.setPreparedByRole(entity.getPreparedByRole());
        agenda.setItems(entity.getItems());
        agenda.setApprovalRequestId(entity.getApprovalRequestId());
        agenda.setStatus(entity.getStatus());
        agenda.setCreatedAt(entity.getCreatedAt());
        agenda.setUpdatedAt(entity.getUpdatedAt());
        return agenda;
    }
}