package com.fuelagent.backend.repository;

import com.fuelagent.backend.model.OwnerSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerSettingsRepository extends JpaRepository<OwnerSettings, Long> {
    Optional<OwnerSettings> findByStationId(Long stationId);
}
