package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.in_game_menus.pause.PauseController;
import com.hallowKnight.in_game_menus.pause.PauseView;
import com.hallowKnight.in_game_menus.inventory.InventoryView;

public class GameScreen implements Screen {
    // وضعیت‌های بازی
    private enum GameState {
        RUNNING, PAUSED, INVENTORY
    }
    private GameState currentState;

    private GameModel model;
    private GameController controller;
    private GameView view;
    private Game game;


    private PauseView pauseView;
    private PauseController pauseController;
    private InventoryView inventoryView;

    public GameScreen(Game game) {
        model = new GameModel();
        controller = new GameController(model, game);
        view = new GameView(model);

        this.game = game;

        currentState = GameState.RUNNING;

        pauseView = new PauseView();
        pauseController = new PauseController(pauseView, new PauseController.PauseListener() {
            @Override
            public void onContinueClicked() {
                resumeGame();
            }

            @Override
            public void onSettingsClicked() {
                System.out.println("Opening Settings...");
                //TODO : programing setting
            }

            @Override
            public void onQuitClicked() {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        inventoryView = new InventoryView(model.getKnightModel());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        checkMenuInputs();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (currentState == GameState.RUNNING) {
            controller.update(delta);

            if (game.getScreen() != this) {
                return;
            }

            view.render(delta);

        } else if (currentState == GameState.PAUSED) {
            view.render(0);
            pauseView.render(delta);

        } else if (currentState == GameState.INVENTORY) {
            view.render(0);
            Gdx.input.setInputProcessor(inventoryView.getStage());
            inventoryView.render(delta);
        }
    }

    private void checkMenuInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (currentState == GameState.RUNNING) {
                currentState = GameState.PAUSED;
                Gdx.input.setInputProcessor(pauseView.getStage());
            } else if (currentState == GameState.PAUSED) {
                resumeGame();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (currentState == GameState.RUNNING) {
                currentState = GameState.INVENTORY;
                Gdx.input.setInputProcessor(inventoryView.getStage());
            } else if (currentState == GameState.INVENTORY) {
                resumeGame();
            }
        }
    }

    private void resumeGame() {
        currentState = GameState.RUNNING;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
        pauseView.getStage().getViewport().update(width, height, true);
        inventoryView.getStage().getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (view != null) view.dispose();
        if (pauseView != null) pauseView.dispose();
        if (inventoryView != null) inventoryView.dispose();
    }

}
