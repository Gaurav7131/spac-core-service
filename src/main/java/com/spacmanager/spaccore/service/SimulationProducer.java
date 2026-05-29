package com.spacmanager.spaccore.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SimulationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SimulationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void triggerSimulation(String spacTicker) {
        String message = "Run heavy math for SPAC: " + spacTicker;

        System.out.println("\n📤 [CORE SERVICE] Firing simulation request into Kafka for: " + spacTicker);

        kafkaTemplate.send("spac-simulations", message);
    }
}