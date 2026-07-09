package com.hallowKnight.Menus.startGameMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.gameplay.core.GameScreen;
import com.hallowKnight.gameplay.core.SaveManager;

public class StartGameController {
    private StartGameModel model;
    private StartGameView view;
    private Game game;

    public StartGameController(StartGameModel model, StartGameView view , Game game) {
        this.model = model;
        this.view = view;
        this.game = game;

        setupListeners();
    }

    private void setupListeners() {
        for (int i = 0; i < view.getSaveButtons().size(); i++) {
            final TextButton btn = view.getSaveButtons().get(i);
            final int buttonIndex = i;

            btn.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    btn.getLabel().setColor(Color.YELLOW);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                    btn.getLabel().setColor(Color.WHITE);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int targetSlot = buttonIndex;

                    if (buttonIndex == 4) {
                        targetSlot = 0;
                        for (int j = 0; j < 4; j++) {
                            if (!SaveManager.saveExists(j)) {
                                targetSlot = j;
                                break;
                            }
                        }

                        Gdx.files.local("saves/saveSlot_" + targetSlot + ".json").delete();
                        System.out.println("Starting a brand NEW game on Slot: " + targetSlot);
                    } else {
                        System.out.println("Loading / Starting Game from Slot: " + targetSlot);
                    }

                    final int finalSlot = targetSlot;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(new GameScreen(game, finalSlot));
                        }
                    });
                }
            });
        }

        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                view.getBackButton().getLabel().setColor(Color.YELLOW);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                view.getBackButton().getLabel().setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Returning to Main Menu...");
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
            }
        });
    }
}
