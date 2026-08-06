package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.FollowUpActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUpActionRepository extends JpaRepository<FollowUpActionEntity, Long> {
}