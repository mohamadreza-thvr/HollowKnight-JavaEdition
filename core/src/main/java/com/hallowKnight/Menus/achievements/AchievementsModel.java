package com.hallowKnight.Menus.achievements;

import java.util.ArrayList;
import java.util.List;

public class AchievementsModel {
    private List<AchievementData> achievements;

    public AchievementsModel() {
        achievements = new ArrayList<>();

        achievements.add(new AchievementData("Completion", "Finish the game.", "Achievements/achievement_pure_completion.png", true));
        achievements.add(new AchievementData("Speedrun", "Finish the game under 2 hours.", "Achievements/speedRun.jpg", true));
        achievements.add(new AchievementData("True Hunter", "Kill all types of enemies.", "Achievements/achievement_Hunter_Journal.png", true));
        achievements.add(new AchievementData("Defeat False Knight", "Defeat the False Knight.", "Achievements/achievement_false_knight #00002521.png", true));
    }

    public List<AchievementData> getAchievements() {
        return achievements;
    }
}
