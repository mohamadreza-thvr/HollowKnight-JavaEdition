package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.hallowKnight.Menus.achievements.AchievementData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AchievementManager {
    private static AchievementManager instance;
    private List<AchievementData> achievements;
    private List<AchievementListener> listeners;
    private Json json;

    public float totalPlayTime = 0f;
    private HashSet<String> killedEnemyTypes = new HashSet<>();

    public interface AchievementListener {
        void onAchievementUnlocked(AchievementData achievement);
    }

    private AchievementManager() {
        listeners = new ArrayList<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadAchievements();
    }

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public void addListener(AchievementListener listener) {
        listeners.add(listener);
    }

    private void loadAchievements() {
        FileHandle file = Gdx.files.local("saves/achievements.json");
        if (file.exists()) {
            achievements = json.fromJson(ArrayList.class, AchievementData.class, file.readString());
        } else {
            achievements = new ArrayList<>();
            achievements.add(new AchievementData("Completion", "Finish the game.", "Achievements/achievement_pure_completion.png", false));
            achievements.add(new AchievementData("Speedrun", "Finish the game under 2 minute.", "Achievements/speedRun.jpg", false));
            achievements.add(new AchievementData("True Hunter", "Kill all types of enemies.", "Achievements/achievement_Hunter_Journal.png", false));
            achievements.add(new AchievementData("Defeat False Knight", "Defeat the False Knight.", "Achievements/achievement_false_knight #00002521.png", false));
            achievements.add(new AchievementData("First Blood", "Kill your first enemy.", "Achievements/achievement_gg__0003__sly.png", false));
        }
    }

    public void saveAchievements() {
        FileHandle file = Gdx.files.local("saves/achievements.json");
        file.writeString(json.prettyPrint(achievements), false);
    }

    public void unlockAchievement(String title) {
        for (AchievementData a : achievements) {
            if (a.getTitle().equals(title) && !a.isUnlocked()) {
                a.setUnlocked(true);
                saveAchievements();
                for (AchievementListener l : listeners) {
                    l.onAchievementUnlocked(a);
                }
                break;
            }
        }
    }

    public void registerEnemyKill(String enemyType) {
        if (killedEnemyTypes.isEmpty()) {
            unlockAchievement("First Blood");
        }
        killedEnemyTypes.add(enemyType);

        if (killedEnemyTypes.size() >= 6) {
            unlockAchievement("True Hunter");
        }
    }

    public List<AchievementData> getAchievements() {
        return achievements;
    }
}
