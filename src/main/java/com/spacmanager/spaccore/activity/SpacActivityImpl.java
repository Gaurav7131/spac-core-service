package com.spacmanager.spaccore.activity;

import com.spacmanager.spaccore.entity.Spac;
import com.spacmanager.spaccore.repository.SpacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpacActivityImpl implements SpacActivity {

    @Autowired
    private SpacRepository spacRepository;

    @Override
    public Spac registerNewSpac(Spac spac) {
        // Convert String to Enum
        spac.setCurrentStage(Spac.SpacStage.valueOf("FORMATION"));
        return spacRepository.save(spac);
    }

    @Override
    public void updateSpacStage(Long spacId, String newStage) {
        Spac spac = spacRepository.findById(spacId)
                .orElseThrow(() -> new RuntimeException("SPAC not found"));

        // Convert String to Enum
        spac.setCurrentStage(Spac.SpacStage.valueOf(newStage.toUpperCase()));
        spacRepository.save(spac);
    }
}