package com.fuelagent.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="owner_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerId;
    private Long stationId;
    private double alertThresholdCents;
    private double competitorRadiusMiles;
    private String fuelTypes;
    private String notificationMethod;

}
