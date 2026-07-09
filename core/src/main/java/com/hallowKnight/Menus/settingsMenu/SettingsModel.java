package com.hallowKnight.Menus.settingsMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsModel {
    private float musicVolume;
    private float sfxVolume;
    private float brightness;
    private String currentLanguage;

    public final float DEFAULT_SFX_VOLUME = 0.8f;

    public SettingsModel() {
        Preferences prefs = Gdx.app.getPreferences("HollowKnightSettings");

        this.musicVolume = prefs.getFloat("musicVol", 0.5f);
        this.sfxVolume = prefs.getFloat("sfxVol", DEFAULT_SFX_VOLUME);
        this.brightness = prefs.getFloat("brightness", 1.0f);
        this.currentLanguage = prefs.getString("language", "English");
    }

    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float musicVolume) { this.musicVolume = musicVolume; }

    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float sfxVolume) { this.sfxVolume = sfxVolume; }

    public float getBrightness() { return brightness; }
    public void setBrightness(float brightness) { this.brightness = brightness; }

    public String getCurrentLanguage() { return currentLanguage; }
    public void setCurrentLanguage(String currentLanguage) { this.currentLanguage = currentLanguage; }
}
