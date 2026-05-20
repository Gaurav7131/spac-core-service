package com.spacmanager.spaccore.activity;

import com.spacmanager.spaccore.entity.Spac;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SpacActivity {
    @ActivityMethod
    Spac registerNewSpac(Spac spac);

    @ActivityMethod
    void updateSpacStage(Long spacId, String newStage);
}