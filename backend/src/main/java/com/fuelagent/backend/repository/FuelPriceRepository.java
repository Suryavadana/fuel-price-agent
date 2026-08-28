package com.fuelagent.backend.repository;

import com.fuelagent.backend.model.FuelPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FuelPriceRepository extends JpaRepository<FuelPrice, Long> {

    Optional<FuelPrice> findFirstByStationIdAndFuelTypeOrderByTimestampDesc(Long stationId, String fuelType);

    Optional<FuelPrice> findFirstByStationIdAndFuelTypeAndTimestampLessThanEqualOrderByTimestampDesc(
            Long stationId, String fuelType, LocalDateTime cutoff);

    List<FuelPrice> findByStationIdAndFuelTypeAndTimestampGreaterThanEqualOrderByTimestampAsc(
            Long stationId, String fuelType, LocalDateTime cutoff);
}