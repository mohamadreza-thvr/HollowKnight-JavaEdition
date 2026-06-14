package com.hallowKnight.Menus.settingsMenu;

public class SettingsModel {
    private float musicVolume;
    private float sfxVolume;
    private String currentLanguage;

    public SettingsModel() {
        // مقادیر پیش‌فرض
        this.musicVolume = 0.5f; // ۵۰ درصد
        this.sfxVolume = 0.8f;   // ۸۰ درصد
        this.currentLanguage = "English";
    }

    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float musicVolume) { this.musicVolume = musicVolume; }

    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float sfxVolume) { this.sfxVolume = sfxVolume; }

    public String getCurrentLanguage() { return currentLanguage; }
    public void setCurrentLanguage(String currentLanguage) { this.currentLanguage = currentLanguage; }
}
