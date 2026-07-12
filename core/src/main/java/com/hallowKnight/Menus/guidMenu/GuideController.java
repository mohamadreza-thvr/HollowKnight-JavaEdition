package com.hallowKnight.Menus.guidMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;

public class GuideController {
    private GuideModel model;
    private GuideView view;
    private Game game;
    private Screen previousScreen;

    public GuideController(GuideModel model, GuideView view, Game game, Screen previousScreen) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.previousScreen = previousScreen;
        setupListeners();
    }

    private void setupListeners() {
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
                if (previousScreen != null) {
                    System.out.println("Returning to Game...");
                    game.setScreen(previousScreen);
                } else {
                    System.out.println("Returning to Main Menu...");
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });
    }
}
