package com.spacmanager.spaccore.activity;

import com.spacmanager.spaccore.entity.Spac;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

//This Page Donot Work but it [list] the  menu,job only like robot
@ActivityInterface
public interface SpacActivity {
    @ActivityMethod // First Job in the menu
    Spac registerNewSpac(Spac spac);

    @ActivityMethod // Second job in the menu
    void updateSpacStage(Long spacId, String newStage);
}