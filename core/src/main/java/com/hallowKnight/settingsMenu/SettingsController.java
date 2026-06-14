package com.hallowKnight.settingsMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;

public class SettingsController {
    private SettingsModel model;
    private SettingsView view;
    private Game game;

    public SettingsController(SettingsModel model, SettingsView view, Game game) {
        this.model = model;
        this.game = game;
        this.view = view;
        setupListeners();
    }

    private void setupListeners() {
        // لیسنر برای اسلایدر موزیک
        view.getMusicSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.setMusicVolume(view.getMusicSlider().getValue());
                System.out.println("Music Vol: " + model.getMusicVolume());
                // اینجا باید ولوم آهنگ بازی را به روز کنید
            }
        });

        // لیسنر برای اسلایدر افکت‌های صوتی
        view.getSfxSlider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.setSfxVolume(view.getSfxSlider().getValue());
                System.out.println("SFX Vol: " + model.getSfxVolume());
            }
        });

        // لیسنر برای دکمه تغییر زبان
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
                // سوییچ بین انگلیسی و فرانسوی (طبق خواسته داک)
                if (model.getCurrentLanguage().equals("English")) {
                    model.setCurrentLanguage("French");
                } else {
                    model.setCurrentLanguage("English");
                }
                // آپدیت متن دکمه
                view.getLanguageButton().setText("Language: " + model.getCurrentLanguage());
            }
        });

        // لیسنر برای دکمه Back
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
                System.out.println("Returning to Main Menu...");
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }
}
