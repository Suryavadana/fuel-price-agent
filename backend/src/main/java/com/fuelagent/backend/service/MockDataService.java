package com.fuelagent.backend.service;

import com.fuelagent.backend.model.FuelPrice;
import com.fuelagent.backend.model.OwnerSettings;
import com.fuelagent.backend.model.Station;
import com.fuelagent.backend.repository.FuelPriceRepository;
import com.fuelagent.backend.repository.OwnerSettingsRepository;
import com.fuelagent.backend.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MockDataService {

    public static final String OWNER_ID = "demo_owner";
    public static final String FUEL_TYPE = "regular";
    private static final int HOURS_OF_HISTORY = 24;
    private static final double BASE_LAT = 30.2672;
    private static final double BASE_LON = -97.7431;

    private record StationSeed(String name, double lat, double lon, boolean own, double start, double trend) {}

    private static final List<StationSeed> STATIONS = List.of(
            new StationSeed("Your Station", BASE_LAT, BASE_LON, true, 3.39, -0.005),
            new StationSeed("Station A", BASE_LAT + 0.010, BASE_LON + 0.006, false, 3.39, -0.030),
            new StationSeed("Station B", BASE_LAT - 0.006, BASE_LON + 0.012, false, 3.37, -0.024),
            new StationSeed("Station C", BASE_LAT + 0.015, BASE_LON - 0.009, false, 3.41, -0.020),
            new StationSeed("Station D", BASE_LAT - 0.012, BASE_LON - 0.014, false, 3.38, -0.002)
    );

    private final StationRepository stationRepository;
    private final FuelPriceRepository fuelPriceRepository;
    private final OwnerSettingsRepository ownerSettingsRepository;

    @Transactional
    public Station seedDemoData(boolean reset) {
        if (reset) {
            fuelPriceRepository.deleteAll();
            ownerSettingsRepository.deleteAll();
            stationRepository.deleteAll();
        }

        LocalDateTime now = LocalDateTime.now();
        Station ownStation = null;
        Map<Long, StationSeed> seedByStationId = new HashMap<>();

        for (StationSeed seed : STATIONS) {
            Station saved = stationRepository.save(Station.builder()
                    .name(seed.name())
                    .address("Demo address")
                    .latitude(seed.lat())
                    .longitude(seed.lon())
                    .ownerId(OWNER_ID)
                    .own(seed.own())
                    .build());
            seedByStationId.put(saved.getId(), seed);
            if (seed.own()) {
                ownStation = saved;
            }
        }

        for (Map.Entry<Long, StationSeed> entry : seedByStationId.entrySet()) {
            Long stationId = entry.getKey();
            StationSeed seed = entry.getValue();
            Random rng = new Random(seed.name().hashCode()); // deterministic per station
            double price = seed.start();

            for (int h = HOURS_OF_HISTORY; h >= 0; h--) {
                LocalDateTime ts = now.minusHours(h);
                if (h <= 3) {
                    price += seed.trend() / 3.0; // concentrate the trend in the last 3 hours
                } else {
                    price += (rng.nextDouble() * 0.008) - 0.004; // small day-to-day noise
                }
                price = Math.max(2.5, round(price));
                fuelPriceRepository.save(FuelPrice.builder()
                        .stationId(stationId)
                        .fuelType(FUEL_TYPE)
                        .price(price)
                        .timestamp(ts)
                        .source("mock")
                        .build());
            }
        }

        ownerSettingsRepository.save(OwnerSettings.builder()
                .ownerId(OWNER_ID)
                .stationId(ownStation.getId())
                .alertThresholdCents(3.0)
                .competitorRadiusMiles(3.0)
                .fuelTypes(FUEL_TYPE)
                .notificationMethod("in_app")
                .build());

        return ownStation;
    }

    @Transactional
    public LocalDateTime simulateTick() {
        LocalDateTime now = LocalDateTime.now();
        List<Station> stations = stationRepository.findByOwnerId(OWNER_ID);
        Random rng = new Random();

        for (Station station : stations) {
            StationSeed seed = STATIONS.stream()
                    .filter(s -> s.name().equals(station.getName()))
                    .findFirst()
                    .orElse(null);

            double basePrice = fuelPriceRepository
                    .findFirstByStationIdAndFuelTypeOrderByTimestampDesc(station.getId(), FUEL_TYPE)
                    .map(FuelPrice::getPrice)
                    .orElse(seed != null ? seed.start() : 3.35);

            double drift = (rng.nextDouble() * 0.025) - 0.015; // slight downward skew
            double newPrice = Math.max(2.5, round(basePrice + drift));

            fuelPriceRepository.save(FuelPrice.builder()
                    .stationId(station.getId())
                    .fuelType(FUEL_TYPE)
                    .price(newPrice)
                    .timestamp(now)
                    .source("mock")
                    .build());
        }
        return now;
    }

    private double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}