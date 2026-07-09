package com.hallowKnight.Menus.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.hallowKnight.UIManager;

public class MainMenuView {
    private Stage stage;

    private Texture backgroundTexture;
    private Image backgroundImage;

    private TextButton startButton;
    private TextButton settingsButton;
    private TextButton guideButton;
    private TextButton achievementsButton;
    private TextButton quitButton;

    public MainMenuView() {
        stage = new Stage(new ExtendViewport(1280, 720));

        backgroundTexture = new Texture(Gdx.files.internal("MainMenu.png"));
        backgroundImage = new Image(backgroundTexture);

        backgroundImage.setFillParent(true);

        stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);

        table.padTop(180);

        UIManager uiManager = UIManager.getInstance();

        startButton = uiManager.createButton("Start Game");
        settingsButton = uiManager.createButton("Settings");
        guideButton = uiManager.createButton("Guide");
        achievementsButton = uiManager.createButton("Achievements");
        quitButton = uiManager.createButton("Quit Game");


        table.add(startButton).row();
        table.add(settingsButton).padTop(5).row();
        table.add(guideButton).padTop(5).row();
        table.add(achievementsButton).padTop(5).row();
        table.add(quitButton).padTop(5).row();

        stage.addActor(table);

        stage.addActor(table);
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
    public TextButton getStartButton() { return startButton; }
    public TextButton getSettingsButton() { return settingsButton; }
    public TextButton getGuideButton() { return guideButton; }
    public TextButton getAchievementsButton() { return achievementsButton; }
    public TextButton getQuitButton() { return quitButton; }
}
