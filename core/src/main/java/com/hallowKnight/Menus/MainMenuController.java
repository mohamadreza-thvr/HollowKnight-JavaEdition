package com.hallowKnight.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuController {
    private MainMenuModel model;
    private MainMenuView view;

    public MainMenuController(MainMenuModel model, MainMenuView view) {
        this.model = model;
        this.view = view;

        setupListeners();
    }



    private void setupListeners() {

        view.getStartButton().addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                view.getStartButton().getLabel().setColor(Color.YELLOW);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                view.getStartButton().getLabel().setColor(Color.WHITE); // برگشت به رنگ عادی
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Game!");
            }
        });

        view.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Opening Settings...");
            }
        });

        view.getQuitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }
}
