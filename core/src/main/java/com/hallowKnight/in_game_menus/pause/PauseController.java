package com.hallowKnight.in_game_menus.pause;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseController {
    private PauseView view;

    public interface PauseListener {
        void onContinueClicked();
        void onGuideClicked();
        void onSettingsClicked();
        void onQuitClicked();
    }

    public PauseController(PauseView view, final PauseListener listener) {
        this.view = view;

        view.getContinueButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onContinueClicked();
            }
        });

        view.getGuideButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onGuideClicked();
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
