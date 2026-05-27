package com.spacmanager.spaccore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spacs")
@Getter
@Setter
public class Spac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private String name;
    private String tickerSymbol;
    private String targetSector;

    // precision and scale=4 standard for financial systems
    @Column(precision = 19, scale = 4)
    private BigDecimal trustValue;

    private String currentStage;

    // 1. These define the database columns
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 2. This sets the date automatically right before a row is CREATED
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 3. This updates the date automatically right before a row is UPDATED
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}