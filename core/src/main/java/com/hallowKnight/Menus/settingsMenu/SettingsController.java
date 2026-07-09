package com.hallowKnight.Menus.settingsMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.gameplay.core.AudioManager;

public class SettingsController {
    private SettingsModel model;
    private SettingsView view;
    private Game game;
    private Screen previousScreen;

    public SettingsController(SettingsModel model, SettingsView view, Game game, Screen previousScreen) {
        this.model = model;
        this.game = game;
        this.view = view;
        this.previousScreen = previousScreen;
        setupListeners();
    }

    private void setupListeners() {
        view.getMusicSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.setMusicVolume(view.getMusicSlider().getValue());
                AudioManager.setMusicVolume(view.getMusicSlider().getValue());
                System.out.println("Music Vol: " + model.getMusicVolume());
            }
        });

        view.getSfxSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.setSfxVolume(view.getSfxSlider().getValue());
                AudioManager.setSFXVolume(view.getSfxSlider().getValue());
                System.out.println("SFX Vol: " + model.getSfxVolume());
            }
        });

        view.getResetSfxButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getResetSfxButton().getLabel().setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                view.getResetSfxButton().getLabel().setColor(Color.WHITE);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float defaultVol = model.DEFAULT_SFX_VOLUME;
                view.getSfxSlider().setValue(defaultVol);
                model.setSfxVolume(defaultVol);
                AudioManager.setSFXVolume(defaultVol);
                System.out.println("SFX Reset to Default: " + defaultVol);
            }
        });

        view.getBrightnessSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newBrightness = view.getBrightnessSlider().getValue();
                model.setBrightness(newBrightness);

                Gdx.app.getPreferences("HollowKnightSettings").putFloat("brightness", newBrightness).flush();
                System.out.println("Brightness Saved: " + newBrightness);
            }
        });

        view.getLanguageButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getLanguageButton().getLabel().setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                view.getLanguageButton().getLabel().setColor(Color.WHITE);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (model.getCurrentLanguage().equals("English")) {
                    model.setCurrentLanguage("French");
                } else {
                    model.setCurrentLanguage("English");
                }
                view.getLanguageButton().setText("Language: " + model.getCurrentLanguage());
            }
        });

        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getBackButton().getLabel().setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                view.getBackButton().getLabel().setColor(Color.WHITE);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Returning to previous screen...");
                game.setScreen(previousScreen);
            }
        });
    }
}
