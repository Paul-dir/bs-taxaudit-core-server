package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.QaSamplingConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaSamplingConfigRepository extends JpaRepository<QaSamplingConfigEntity, Long> {
}