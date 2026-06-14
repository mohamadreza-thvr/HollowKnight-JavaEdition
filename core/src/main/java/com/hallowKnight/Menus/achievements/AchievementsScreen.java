package com.hallowKnight.Menus.achievements;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class AchievementsScreen implements Screen {
    private AchievementsModel model;
    private AchievementsView view;
    private AchievementsController controller;
    private Game game;

    public AchievementsScreen(Game game) {
        model = new AchievementsModel();
        view = new AchievementsView(model);
        this.game = game;
        controller = new AchievementsController(model, view, game);
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
