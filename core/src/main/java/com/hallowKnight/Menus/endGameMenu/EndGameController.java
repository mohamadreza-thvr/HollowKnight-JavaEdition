package com.hallowKnight.Menus.endGameMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.gameplay.core.GameScreen;

public class EndGameController {
    private EndGameView view;
    private Game game;
    private int slotIndex;

    public EndGameController(EndGameView view, Game game, int slotIndex) {
        this.view = view;
        this.game = game;
        this.slotIndex = slotIndex;
        setupListeners();
    }

    private void setupListeners() {
        view.getRestartButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getRestartButton().getLabel().setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                view.getRestartButton().getLabel().setColor(Color.WHITE);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.files.local("saves/saveSlot_" + slotIndex + ".json").delete();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, slotIndex));
                    }
                });
            }
        });

        view.getMainMenuButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getMainMenuButton().getLabel().setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                view.getMainMenuButton().getLabel().setColor(Color.WHITE);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
            }
        });
    }
}
