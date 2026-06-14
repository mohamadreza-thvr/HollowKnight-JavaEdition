package com.hallowKnight.Menus.settingsMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen implements Screen {
    private SettingsModel model;
    private SettingsView view;
    private SettingsController controller;
    private Game game;

    public SettingsScreen(Game game) {
        model = new SettingsModel();
        view = new SettingsView(model);
        this.game = game;
        controller = new SettingsController(model, view, game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(view.getStage());
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
