package com.hallowKnight.in_game_menus.pause;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseController {
    private PauseView view;

    public interface PauseListener {
        void onContinueClicked();
        void onSettingsClicked();
        void onQuitClicked();
    }

    public PauseController(PauseView view, PauseListener listener) {
        this.view = view;

        view.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onContinueClicked();
            }
        });

        view.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onSettingsClicked();
            }
        });

        view.getSaveQuitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onQuitClicked();
            }
        });
    }
}
