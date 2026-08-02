package com.mor.itas.planning.persistence.jpa.repository;

import com.itas.bs.taxaudit.persistence.jpa.entity.OutboxEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findByPublishedFalseOrderByCreatedAtAsc(Pageable pageable);
}
