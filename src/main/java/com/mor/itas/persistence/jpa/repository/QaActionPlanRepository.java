package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaActionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaActionPlanRepository extends JpaRepository<QaActionPlanEntity, Long> {
}