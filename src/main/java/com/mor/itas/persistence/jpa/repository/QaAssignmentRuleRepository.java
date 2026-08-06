package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaAssignmentRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaAssignmentRuleRepository extends JpaRepository<QaAssignmentRuleEntity, Long> {
}