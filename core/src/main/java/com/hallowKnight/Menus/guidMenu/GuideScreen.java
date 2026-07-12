package com.hallowKnight.Menus.guidMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GuideScreen implements Screen {
    private GuideModel model;
    private GuideView view;
    private Game game;
    private GuideController controller;
    private Screen previousScreen;

    public GuideScreen(Game game) {
        this(game, null);
    }

    public GuideScreen(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        model = new GuideModel();
        view = new GuideView(model);
        controller = new GuideController(model, view, game, previousScreen);
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
