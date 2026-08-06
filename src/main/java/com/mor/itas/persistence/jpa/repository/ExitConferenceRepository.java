package com.mor.itas.persistence.jpa.repository;

import com.mor.itas.persistence.jpa.entity.ExitConferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitConferenceRepository extends JpaRepository<ExitConferenceEntity, Long> {
}