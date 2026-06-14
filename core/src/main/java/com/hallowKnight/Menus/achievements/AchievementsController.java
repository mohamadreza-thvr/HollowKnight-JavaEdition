package com.hallowKnight.Menus.achievements;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;

public class AchievementsController {
    private AchievementsModel model;
    private AchievementsView view;
    private Game game;

    public AchievementsController(AchievementsModel model, AchievementsView view, Game game) {
        this.model = model;
        this.view = view;
        this.game = game;
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
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }
}
