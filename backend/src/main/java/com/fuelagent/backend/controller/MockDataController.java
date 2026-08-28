package com.fuelagent.backend.controller;

import com.fuelagent.backend.model.Station;
import com.fuelagent.backend.service.MockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockDataController {

    private final MockDataService mockDataService;

    @PostMapping("/seed")
    public Map<String, Object> seed() {
        Station station = mockDataService.seedDemoData(true);
        return Map.of(
                "status", "seeded",
                "ownStationId", station.getId(),
                "ownerId", MockDataService.OWNER_ID
        );
    }

    @PostMapping("/tick")
    public Map<String, Object> tick() {
        LocalDateTime ts = mockDataService.simulateTick();
        return Map.of("status", "ticked", "asOf", ts.toString());
    }
}