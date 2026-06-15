package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    private GameModel model;
    private GameController controller;
    private GameView view;

    public GameScreen() {
        model = new GameModel();
        controller = new GameController(model);
        view = new GameView(model);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {

        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.render();
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        view.dispose();
    }
}
