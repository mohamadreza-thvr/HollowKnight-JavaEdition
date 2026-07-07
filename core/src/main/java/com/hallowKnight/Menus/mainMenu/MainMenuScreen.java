package com.hallowKnight.Menus.mainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.hallowKnight.gameplay.core.AudioManager; // ⚠️ ایمپورت اضافه شد

public class MainMenuScreen implements Screen {
    private MainMenuModel model;
    private MainMenuView view;
    private MainMenuController controller;

    public MainMenuScreen(Game game) {
        model = new MainMenuModel();
        view = new MainMenuView();
        controller = new MainMenuController(model, view, game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(view.getStage());

        // پخش موسیقی منوی اصلی به محض ورود به این صفحه
        AudioManager.playMusic("audio/music/menus.mp3");
    }

    @Override
    public void render(float delta) {
        AudioManager.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        view.getStage().getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        view.dispose();
    }
}
