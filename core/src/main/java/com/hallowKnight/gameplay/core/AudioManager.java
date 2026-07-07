package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private static Music currentMusic;
    private static String currentMusicPath = "";

    private static String nextMusicPath = "";
    private static boolean isFading = false;
    private static float fadeTimer = 0f;
    private static final float FADE_DURATION = 2.0f;

    private static float SFXVolume = 1.0f;
    private static float MusicVolume = 1.0f;

    private static Sound nailSlash;
    private static Sound takeDamage;
    private static Sound soulGain;
    private static Sound focusHeal;
    private static Sound scream;
    private static Sound dash;


    public static void loadSFX() {
        nailSlash  = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/nail_slash.wav"));
        takeDamage = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/knight_damage.wav"));
        soulGain   = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/soul_gain.wav"));
        focusHeal  = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/focus_heal.wav"));
        scream     = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_scream_spell.wav"));
        dash       = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hero_dash.wav"));
    }

    public static void update(float delta) {
        if (isFading && currentMusic != null) {
            fadeTimer -= delta;

            if (fadeTimer <= 0) {
                isFading = false;
                startNewMusic(nextMusicPath);
            } else {
                float volume = (fadeTimer / FADE_DURATION) * MusicVolume;
                currentMusic.setVolume(Math.max(0, volume));
            }
        }
    }

    public static void playMusic(String filePath) {
        if (currentMusicPath.equals(filePath)) return;

        if (currentMusic != null && currentMusic.isPlaying()) {
            nextMusicPath = filePath;
            isFading = true;
            fadeTimer = FADE_DURATION;
        } else {
            startNewMusic(filePath);
        }
    }

    private static void startNewMusic(String filePath) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }

        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        currentMusic.setLooping(true);
        currentMusic.setVolume(MusicVolume);
        currentMusic.play();

        currentMusicPath = filePath;
    }

    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public static void playNailSlash() {
        if (nailSlash != null) nailSlash.play(SFXVolume);
    }

    public static void playTakeDamage() {
        if (takeDamage != null) takeDamage.play(SFXVolume);
    }

    public static void playSoulGain() {
        if (soulGain != null) soulGain.play(SFXVolume);
    }

    public static void playFocus() {
        if (focusHeal != null) focusHeal.play(SFXVolume);
    }

    public static void playDash() {
        if (dash != null) dash.play(SFXVolume);
    }

    public static void playScream() {
        if (scream != null) scream.play(SFXVolume);
    }

    public static void dispose() {
        if (currentMusic != null) currentMusic.dispose();
        if (nailSlash != null) nailSlash.dispose();
        if (takeDamage != null) takeDamage.dispose();
        if (soulGain != null) soulGain.dispose();
        if (focusHeal != null) focusHeal.dispose();
        if (scream != null) scream.dispose();
        if (dash != null) dash.dispose();
    }

    public static float getMusicVolume() {
        return MusicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        MusicVolume = musicVolume;

        if (currentMusic != null && !isFading) {
            currentMusic.setVolume(MusicVolume);
        }
    }

    public static float getSFXVolume() {
        return SFXVolume;
    }

    public static void setSFXVolume(float sfxVolume) {
        SFXVolume = sfxVolume;
    }
}
