package com.spacmanager.spaccore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

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

    // Precision 19, Scale 4 is standard for financial systems
    @Column(precision = 19, scale = 4)
    private BigDecimal trustValue;

    private String currentStage;
}
