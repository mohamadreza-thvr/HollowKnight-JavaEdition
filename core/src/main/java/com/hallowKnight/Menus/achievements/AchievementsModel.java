package com.hallowKnight.Menus.achievements;

import com.hallowKnight.gameplay.core.AchievementManager;
import java.util.List;

public class AchievementsModel {
    private List<AchievementData> achievements;

    public AchievementsModel() {
        achievements = AchievementManager.getInstance().getAchievements();
    }

    public List<AchievementData> getAchievements() {
        return achievements;
    }
}
