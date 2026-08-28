package com.fuelagent.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stationId;
    private String fuelType;
    private double price;
    private LocalDateTime timestamp;
    private String source;
}
