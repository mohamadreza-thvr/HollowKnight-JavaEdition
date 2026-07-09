package com.hallowKnight.Menus.endGameMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.core.AudioManager;

public class EndGameScreen implements Screen {
    private EndGameModel model;
    private EndGameView view;
    private EndGameController controller;
    private Game game;

    public EndGameScreen(Game game, KnightModel knight, int slotIndex) {
        this.game = game;
        this.model = new EndGameModel(knight);
        this.view = new EndGameView(model);
        this.controller = new EndGameController(view, game, slotIndex);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(view.getStage());

        AudioManager.playMusic("audio/music/victory_theme.mp3");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        view.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        view.getStage().getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        view.dispose();
    }
}
