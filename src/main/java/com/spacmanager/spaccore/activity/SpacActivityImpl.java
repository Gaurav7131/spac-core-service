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
        spac.setCurrentStage("FORMATION");// Just start i.e Formation
        return spacRepository.save(spac);// save into Spacrepo
    }

    @Override
    public void updateSpacStage(Long spacId, String newStage) {
        Spac spac = spacRepository.findById(spacId)
                .orElseThrow(() -> new RuntimeException("SPAC not found"));/*
                                                                            * find spac by spacID if not found Encounter
                                                                            * RunTimeException and msg SPAC Not Found
                                                                            * and set currentsatge as NewStage and save
                                                                            * them into Spacrepo
                                                                            */

        spac.setCurrentStage(newStage);
        spacRepository.save(spac);
    }
}