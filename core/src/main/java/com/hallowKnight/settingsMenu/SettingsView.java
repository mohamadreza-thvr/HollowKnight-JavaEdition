package com.hallowKnight.settingsMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.UIManager;

public class SettingsView {
    private Stage stage;
    private Texture backgroundTexture;

    private Slider musicSlider;
    private Slider sfxSlider;
    private TextButton languageButton;
    private TextButton backButton;

    public SettingsView(SettingsModel model) {

        stage = new Stage(new StretchViewport(1280, 720));

        backgroundTexture = new Texture(Gdx.files.internal("SettingMenu.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        UIManager uiManager = UIManager.getInstance();

        Table centerTable = new Table();
        centerTable.setFillParent(true);
        centerTable.padTop(150);

        Label musicLabel = uiManager.createLabel("Music Volume");
        Label sfxLabel = uiManager.createLabel("SFX Volume");

        musicSlider = new Slider(0f, 1f, 0.1f, false, uiManager.getSkin());
        musicSlider.setValue(model.getMusicVolume());

        sfxSlider = new Slider(0f, 1f, 0.1f, false, uiManager.getSkin());
        sfxSlider.setValue(model.getSfxVolume());

        languageButton = uiManager.createButton("Language: " + model.getCurrentLanguage());

        centerTable.add(musicLabel).padRight(20).padBottom(20);
        centerTable.add(musicSlider).width(300).padBottom(20).row();

        centerTable.add(sfxLabel).padRight(20).padBottom(40);
        centerTable.add(sfxSlider).width(300).padBottom(40).row();

        centerTable.add(languageButton).colspan(2).padTop(20).row();

        stage.addActor(centerTable);

        Table topLeftTable = new Table();
        topLeftTable.setFillParent(true);
        topLeftTable.top().left();

        backButton = uiManager.createButton("Back");
        topLeftTable.add(backButton).pad(30);

        stage.addActor(topLeftTable);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }

    public Stage getStage() { return stage; }
    public Slider getMusicSlider() { return musicSlider; }
    public Slider getSfxSlider() { return sfxSlider; }
    public TextButton getLanguageButton() { return languageButton; }
    public TextButton getBackButton() { return backButton; }
}
