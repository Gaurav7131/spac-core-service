package com.spacmanager.spaccore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.*; // Importing all lombok annotations
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spac", indexes = @Index(name = "idx_ticker", columnList = "tickerSymbol"))
@Getter
@Setter // Added this so setCurrentStage(SpacStage) is generated
@NoArgsConstructor
@AllArgsConstructor // Added for convenience
public class Spac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    public enum SpacStage {
        PRE_IPO, IPO, DE_SPAC, COMPLETED, FORMATION
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpacStage currentStage;

    @Column(nullable = false)
    private String tickerSymbol;

    private String name;
    private String targetSector;

    @Column(precision = 19, scale = 4)
    private BigDecimal trustValue;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}