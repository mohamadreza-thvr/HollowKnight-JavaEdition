package com.hallowKnight.Menus.settingsMenu;

import com.badlogic.gdx.Gdx;
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
    private Slider brightnessSlider;
    private TextButton resetSfxButton;
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
        Label brightnessLabel = uiManager.createLabel("Brightness");

        musicSlider = new Slider(0f, 1f, 0.1f, false, uiManager.getSkin());
        musicSlider.setValue(model.getMusicVolume());

        sfxSlider = new Slider(0f, 1f, 0.1f, false, uiManager.getSkin());
        sfxSlider.setValue(model.getSfxVolume());

        brightnessSlider = new Slider(0.1f, 1f, 0.1f, false, uiManager.getSkin());
        brightnessSlider.setValue(model.getBrightness());

        resetSfxButton = uiManager.createButton("Reset SFX");
        resetSfxButton.getLabel().setFontScale(0.8f);

        languageButton = uiManager.createButton("Language: " + model.getCurrentLanguage());

        // چیدمان Music
        centerTable.add(musicLabel).padRight(20).padBottom(20);
        centerTable.add(musicSlider).width(300).padBottom(20).colspan(2).row();

        centerTable.add(sfxLabel).padRight(20).padBottom(20);
        centerTable.add(sfxSlider).width(300).padBottom(20);
        centerTable.add(resetSfxButton).padLeft(20).padBottom(20).row();

        // چیدمان Brightness
        centerTable.add(brightnessLabel).padRight(20).padBottom(40);
        centerTable.add(brightnessSlider).width(300).padBottom(40).colspan(2).row();

        // چیدمان Language
        centerTable.add(languageButton).colspan(3).padTop(20).row();

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
    public Slider getBrightnessSlider() { return brightnessSlider; }
    public TextButton getResetSfxButton() { return resetSfxButton; }
    public TextButton getLanguageButton() { return languageButton; }
    public TextButton getBackButton() { return backButton; }
}
