package com.hallowKnight.in_game_menus.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.UIManager;

public class PauseView {
    private Stage stage;
    private TextButton continueButton;
    private TextButton settingsButton;
    private TextButton saveQuitButton;
    private Texture darkOverlay;

    public PauseView() {
        stage = new Stage(new StretchViewport(1280, 720));
        UIManager uiManager = UIManager.getInstance();


        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.7f));
        pixmap.fill();
        darkOverlay = new Texture(pixmap);
        pixmap.dispose();

        Image backgroundImage = new Image(darkOverlay);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // جدول دکمه‌ها
        Table table = new Table();
        table.setFillParent(true);

        Label title = uiManager.createLabel("PAUSE");
        title.setFontScale(1.5f);
        title.setColor(Color.LIGHT_GRAY);

        continueButton = uiManager.createButton("Continue");
        settingsButton = uiManager.createButton("Settings");
        saveQuitButton = uiManager.createButton("Save & Quit");

        table.add(title).padBottom(50).row();
        table.add(continueButton).padBottom(20).row();
        table.add(settingsButton).padBottom(20).row();
        table.add(saveQuitButton).padBottom(20).row();

        stage.addActor(table);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() { return stage; }
    public TextButton getContinueButton() { return continueButton; }
    public TextButton getSettingsButton() { return settingsButton; }
    public TextButton getSaveQuitButton() { return saveQuitButton; }

    public void dispose() {
        stage.dispose();
        darkOverlay.dispose();
    }
}
