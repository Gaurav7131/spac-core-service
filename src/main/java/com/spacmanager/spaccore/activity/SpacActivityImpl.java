package com.spacmanager.spaccore.activity;

import com.spacmanager.spaccore.entity.Spac;
import com.spacmanager.spaccore.repository.SpacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SpacActivityImpl implements SpacActivity {

    @Autowired
    private SpacRepository spacRepository;

    @Override
    @Transactional
    public Spac registerNewSpac(Spac spac) {
        // Convert String to Enum
        spac.setCurrentStage(Spac.SpacStage.valueOf("FORMATION"));
        return spacRepository.save(spac);
    }

    @Override
    @Transactional
    public void updateSpacStage(Long spacId, String newStage) {
        Spac spac = spacRepository.findById(spacId)
                .orElseThrow(() -> new RuntimeException("SPAC not found" + spacId));

        // Convert String to Enum
        spac.setCurrentStage(Spac.SpacStage.valueOf(newStage.toUpperCase()));
        spacRepository.save(spac);
    }

    @Override
    public void processRefund(Long spacId, String reason) {
        // Log for observability
        System.out.println(" Processing refund for SPAC " + spacId + ". Reason: " + reason);

        // Logic: Find SPAC and update stage to REFUNDED
        Spac spac = spacRepository.findById(spacId)
                .orElseThrow(() -> new RuntimeException("SPAC not found"));
        spac.setCurrentStage(Spac.SpacStage.valueOf("REFUNDED"));
        spacRepository.save(spac);
    }

    @Override
    public void executeMerger(Long spacId) {
        System.out.println(" Executing merger for SPAC " + spacId);

        // Logic: Finalize merger
        Spac spac = spacRepository.findById(spacId)
                .orElseThrow(() -> new RuntimeException("SPAC not found"));
        spac.setCurrentStage(Spac.SpacStage.valueOf("MERGED"));
        spacRepository.save(spac);
    }
}