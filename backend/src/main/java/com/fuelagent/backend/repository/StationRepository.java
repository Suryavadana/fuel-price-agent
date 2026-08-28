package com.fuelagent.backend.repository;

import com.fuelagent.backend.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station,Long> {
    List<Station> findByOwnerId(String ownerId);
}
