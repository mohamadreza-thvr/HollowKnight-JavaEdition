package com.hallowKnight.Menus.endGameMenu;

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

public class EndGameView {
    private Stage stage;
    private TextButton restartButton;
    private TextButton mainMenuButton;
    private Texture bgTexture;

    public EndGameView(EndGameModel model) {
        stage = new Stage(new StretchViewport(1280, 720));
        UIManager uiManager = UIManager.getInstance();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        bgTexture = new Texture(pixmap);
        pixmap.dispose();

        Image bgImage = new Image(bgTexture);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = uiManager.createLabel("VICTORY");
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.GOLD);

        Label killsLabel = uiManager.createLabel("Enemies Killed: " + model.kills);
        Label deathsLabel = uiManager.createLabel("Total Deaths: " + model.deaths);
        Label timeLabel = uiManager.createLabel("Total Time: " + model.time);

        restartButton = uiManager.createButton("Restart Game");
        mainMenuButton = uiManager.createButton("Main Menu");

        table.add(titleLabel).padBottom(50).colspan(2).row();
        table.add(killsLabel).padBottom(20).colspan(2).row();
        table.add(deathsLabel).padBottom(20).colspan(2).row();
        table.add(timeLabel).padBottom(50).colspan(2).row();

        table.add(restartButton).padRight(20);
        table.add(mainMenuButton).padLeft(20);

        stage.addActor(table);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        bgTexture.dispose();
    }

    public Stage getStage() { return stage; }
    public TextButton getRestartButton() { return restartButton; }
    public TextButton getMainMenuButton() { return mainMenuButton; }
}
