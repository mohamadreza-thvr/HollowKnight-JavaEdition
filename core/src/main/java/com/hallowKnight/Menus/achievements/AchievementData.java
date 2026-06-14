package com.hallowKnight.Menus.achievements;

public class AchievementData {
    private String title;
    private String description;
    private String iconPath;
    private boolean isUnlocked;

    public AchievementData(String title, String description, String iconPath, boolean isUnlocked) {
        this.title = title;
        this.description = description;
        this.iconPath = iconPath;
        this.isUnlocked = isUnlocked;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIconPath() { return iconPath; }
    public boolean isUnlocked() { return isUnlocked; }

    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
}
