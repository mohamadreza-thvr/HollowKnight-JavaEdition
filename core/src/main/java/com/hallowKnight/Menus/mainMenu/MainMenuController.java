package com.hallowKnight.Menus.mainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.settingsMenu.SettingsScreen;
import com.hallowKnight.startGameMenu.StartGameScreen;

public class MainMenuController {
    private MainMenuModel model;
    private MainMenuView view;
    private Game game;

    public MainMenuController(MainMenuModel model, MainMenuView view, Game game) {
        this.model = model;
        this.view = view;
        this.game = game;

        setupListeners();
    }



    private void setupListeners() {

        view.getStartButton().addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                view.getStartButton().getLabel().setColor(Color.YELLOW);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                view.getStartButton().getLabel().setColor(Color.WHITE); // برگشت به رنگ عادی
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartGameScreen(game));
            }
        });

        view.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        view.getQuitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }
}
