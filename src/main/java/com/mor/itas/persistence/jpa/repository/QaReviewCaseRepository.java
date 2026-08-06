package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaReviewCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaReviewCaseRepository extends JpaRepository<QaReviewCaseEntity, Long> {
}