package com.spacmanager.spaccore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    // Creation & Updated Date and Time Purpose
    @CreationTimestamp

    @Column(updatable = false)

    private LocalDateTime createdAt;

    @UpdateTimestamp

    private LocalDateTime updatedAt;

    private String name;
    private String tickerSymbol;
    private String targetSector;

    // Precision 19, Scale 4 is standard for financial systems
    @Column(precision = 19, scale = 4)
    private BigDecimal trustValue;

    private String currentStage;
}