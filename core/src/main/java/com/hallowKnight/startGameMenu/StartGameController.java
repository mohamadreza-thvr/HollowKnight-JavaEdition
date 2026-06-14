package com.hallowKnight.startGameMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;

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
        // ۱. تنظیم لیسنر برای دکمه‌های سیو (تغییر رنگ و کلیک)
        for (int i = 0; i < view.getSaveButtons().size(); i++) {
            final TextButton btn = view.getSaveButtons().get(i);
            final int slotIndex = i; // برای اینکه بفهمیم کدام سیو انتخاب شده

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
                    System.out.println("Loading / Starting Game from Slot: " + model.getSaveSlots().get(slotIndex));
                    // TODO: after writing the game screen
                    // game.setScreen(new GameScreen(game, slotIndex));
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
                System.out.println("Returning to Main Menu...");
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }
}
