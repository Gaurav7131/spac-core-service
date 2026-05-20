package com.spacmanager.spaccore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "spacs")
@Data
public class Spac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String tickerSymbol;
    private String targetSector;
    private Double trustValue;
    private String currentStage;
}