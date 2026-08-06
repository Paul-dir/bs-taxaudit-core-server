package com.mor.itas.persistence.adapter;

import com.mor.itas.domain.model.FollowUpVerification;
import com.mor.itas.persistence.jpa.entity.FollowUpVerificationEntity;
import com.mor.itas.persistence.jpa.repository.FollowUpVerificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowUpVerificationAdapter {
    private final FollowUpVerificationRepository repository;

    public FollowUpVerificationAdapter(FollowUpVerificationRepository repository) {
        this.repository = repository;
    }

    public FollowUpVerification save(FollowUpVerification verification) {
        FollowUpVerificationEntity entity = toEntity(verification);
        FollowUpVerificationEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    public List<FollowUpVerification> findByFollowUpActionId(Long followUpActionId) {
        return repository.findAll().stream()
                .filter(e -> e.getFollowUpActionId().equals(followUpActionId))
                .map(this::toDomain)
                .toList();
    }

    private FollowUpVerificationEntity toEntity(FollowUpVerification verification) {
        FollowUpVerificationEntity entity = new FollowUpVerificationEntity();
        entity.setId(verification.getId());
        entity.setFollowUpActionId(verification.getFollowUpActionId());
        entity.setVerifiedBy(verification.getVerifiedBy());
        entity.setVerifiedAt(verification.getVerifiedAt());
        entity.setOutcome(verification.getOutcome());
        entity.setComments(verification.getComments());
        entity.setCreatedAt(verification.getCreatedAt());
        return entity;
    }

    private FollowUpVerification toDomain(FollowUpVerificationEntity entity) {
        FollowUpVerification verification = new FollowUpVerification();
        verification.setId(entity.getId());
        verification.setFollowUpActionId(entity.getFollowUpActionId());
        verification.setVerifiedBy(entity.getVerifiedBy());
        verification.setVerifiedAt(entity.getVerifiedAt());
        verification.setOutcome(entity.getOutcome());
        verification.setComments(entity.getComments());
        verification.setCreatedAt(entity.getCreatedAt());
        return verification;
    }
}