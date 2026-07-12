package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.Menus.achievements.AchievementData;
import com.hallowKnight.Menus.guidMenu.GuideScreen;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.UIManager;
import com.hallowKnight.in_game_menus.pause.PauseController;
import com.hallowKnight.in_game_menus.pause.PauseView;
import com.hallowKnight.in_game_menus.inventory.InventoryView;

public class GameScreen implements Screen {

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

    private Stage popupStage;
    private Table popupTable;
    private Label popupLabel;
    private float popupTimer = 0f;

    public GameScreen(Game game, final int slotIndex) {
        model = new GameModel();

        GameData data = SaveManager.load(slotIndex);
        if (data != null) {
            System.out.println("Save found! Loading Slot: " + slotIndex);
            model.loadMap(data.currentMapPath);
            model.getKnightModel().setX(data.playerX);
            model.getKnightModel().setY(data.playerY);
            model.getKnightModel().setCurrentHealth(data.currentHealth);
            model.getKnightModel().setMaxHealth(data.maxHealth);
            model.getKnightModel().setCurrentSoul(data.currentSoul);

            model.getKnightModel().setTotalKills(data.totalKills);
            model.getKnightModel().setTotalDeaths(data.totalDeaths);
            model.getKnightModel().setTotalTime(data.totalTime);

            AchievementManager.getInstance().totalPlayTime = data.totalTime;

            if (data.isBossDead && model.getBoss() != null) {
                model.getBoss().setDead(true);
            }
        } else {
            AchievementManager.getInstance().totalPlayTime = 0f;
        }

        controller = new GameController(model, game, slotIndex);
        view = new GameView(model);
        this.game = game;
        currentState = GameState.RUNNING;

        setupPauseMenu(slotIndex);
        setupAchievementPopup();

        inventoryView = new InventoryView(model.getKnightModel());
    }

    private void setupPauseMenu(final int slotIndex) {
        pauseView = new PauseView();
        pauseController = new PauseController(pauseView, new PauseController.PauseListener() {
            @Override
            public void onContinueClicked() {
                resumeGame();
            }

            @Override
            public void onGuideClicked() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GuideScreen(game, GameScreen.this));
                    }
                });
            }

            @Override
            public void onSettingsClicked() {
                System.out.println("Opening Settings from Game...");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new com.hallowKnight.Menus.settingsMenu.SettingsScreen(game, GameScreen.this));
                    }
                });
            }

            @Override
            public void onQuitClicked() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        GameData saveData = new GameData();
                        saveData.playerX = model.getKnightModel().getX();
                        saveData.playerY = model.getKnightModel().getY();
                        saveData.currentHealth = model.getKnightModel().getCurrentHealth();
                        saveData.maxHealth = model.getKnightModel().getMaxHealth();
                        saveData.currentSoul = model.getKnightModel().getCurrentSoul();
                        saveData.currentMapPath = model.getCurrentMapPath();
                        saveData.isBossDead = (model.getBoss() == null || model.getBoss().isDead());

                        saveData.totalKills = model.getKnightModel().getTotalKills();
                        saveData.totalDeaths = model.getKnightModel().getTotalDeaths();
                        saveData.totalTime = model.getKnightModel().getTotalTime();

                        SaveManager.save(saveData, slotIndex);
                        System.out.println("Game Saved Successfully in Slot: " + slotIndex);

                        game.setScreen(new MainMenuScreen(game));
                        dispose();
                    }
                });
            }
        });
    }

    private void setupAchievementPopup() {
        popupStage = new Stage(new StretchViewport(1280, 720));
        popupTable = new Table();
        popupTable.top().right().pad(30);
        popupTable.setFillParent(true);

        popupLabel = UIManager.getInstance().createLabel("");
        popupLabel.setColor(Color.GOLD);
        popupLabel.setFontScale(0.8f);

        popupTable.add(popupLabel);
        popupStage.addActor(popupTable);
        popupTable.setVisible(false);

        AchievementManager.getInstance().addListener(new AchievementManager.AchievementListener() {
            @Override
            public void onAchievementUnlocked(AchievementData achievement) {
                popupLabel.setText("Achievement Unlocked!\n" + achievement.getTitle());
                popupTable.setVisible(true);
                popupTimer = 4.0f;
                AudioManager.playFocus();
            }
        });
    }

    @Override
    public void show() {
        if (currentState == GameState.PAUSED && pauseView != null) {
            Gdx.input.setInputProcessor(pauseView.getStage());
        } else if (currentState == GameState.INVENTORY && inventoryView != null) {
            Gdx.input.setInputProcessor(inventoryView.getStage());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void render(float delta) {
        AudioManager.update(delta);

        checkMenuInputs();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (currentState == GameState.RUNNING) {
            AchievementManager.getInstance().totalPlayTime += delta;

            model.getKnightModel().setTotalTime(model.getKnightModel().getTotalTime() + delta);

            controller.update(delta);

            if (game.getScreen() != this) {
                return;
            }

            view.render(delta);

            if (popupTimer > 0) {
                popupTimer -= delta;
                if (popupTimer <= 0) popupTable.setVisible(false);
                popupStage.act(delta);
                popupStage.draw();
            }

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
        if (popupStage != null) {
            popupStage.getViewport().update(width, height, true);
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (view != null) view.dispose();
        if (pauseView != null) pauseView.dispose();
        if (inventoryView != null) inventoryView.dispose();
        if (popupStage != null) popupStage.dispose();
    }
}
