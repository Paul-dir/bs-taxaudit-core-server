package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaFindingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaFindingRepository extends JpaRepository<QaFindingEntity, Long> {
}