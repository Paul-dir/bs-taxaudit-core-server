package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaReportRepository extends JpaRepository<QaReportEntity, Long> {
}