package com.fuelagent.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerId;
    private Long stationId;
    private String alertType;
    private String severity;
    private String title;

    @Column(length = 2000)
    private String message;

    private LocalDateTime createdAt;
    private boolean sent;
    private String channel;

    //JSON snapshot of the market situation this alert was genertated from.
    @Column(length = 4000)
    private String metaJson;
}
