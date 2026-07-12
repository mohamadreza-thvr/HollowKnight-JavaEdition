package com.hallowKnight.Menus.guidMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.UIManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuideView {
    private Stage stage;
    private Texture backgroundTexture;
    private TextButton backButton;
    private List<Texture> loadedKeyTextures = new ArrayList<>();

    public GuideView(GuideModel model) {
        stage = new Stage(new StretchViewport(1280, 720));

        backgroundTexture = new Texture(Gdx.files.internal("GuidMenu.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        UIManager uiManager = UIManager.getInstance();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.padTop(240);

        Table controlsTable = new Table();
        Label ctrlTitle = uiManager.createLabel("--- CONTROLS ---");
        ctrlTitle.setColor(Color.GOLD);
        controlsTable.add(ctrlTitle).colspan(3).padBottom(5).row();

        for (Map.Entry<String, String> entry : model.getControls().entrySet()) {
            Label actionLabel = uiManager.createLabel(entry.getKey());
            actionLabel.setFontScale(0.85f);

            Texture keyTex = new Texture(Gdx.files.internal(entry.getValue()));
            loadedKeyTextures.add(keyTex);
            Image keyImage = new Image(keyTex);

            controlsTable.add(actionLabel).left();
            Label dots = uiManager.createLabel(" .... ");
            dots.setColor(Color.DARK_GRAY);
            controlsTable.add(dots).center().padLeft(10).padRight(10);
            controlsTable.add(keyImage).size(40, 35).right().padBottom(5).row();
        }

        Table cheatsTable = new Table();
        Label cheatTitle = uiManager.createLabel("--- CHEAT CODES ---");
        cheatTitle.setColor(Color.GOLD);
        cheatsTable.add(cheatTitle).colspan(3).padBottom(20).row();

        Texture ctrlTex = new Texture(Gdx.files.internal("keys/key-ctrl.png"));
        loadedKeyTextures.add(ctrlTex);

        for (Map.Entry<String, String> entry : model.getCheatCodes().entrySet()) {
            Label actionLabel = uiManager.createLabel(entry.getKey());
            actionLabel.setFontScale(0.85f);

            Texture letterTex = new Texture(Gdx.files.internal(entry.getValue()));
            loadedKeyTextures.add(letterTex);

            Image ctrlImage = new Image(ctrlTex);
            Image letterImage = new Image(letterTex);

            Label plusLabel = uiManager.createLabel(" + ");
            plusLabel.setFontScale(0.85f);

            Table comboTable = new Table();
            comboTable.add(ctrlImage).size(45, 35);
            comboTable.add(plusLabel).padLeft(5).padRight(5);
            comboTable.add(letterImage).size(35, 35);

            cheatsTable.add(actionLabel).left();
            Label dots = uiManager.createLabel(" .... ");
            dots.setColor(Color.DARK_GRAY);
            cheatsTable.add(dots).center().padLeft(10).padRight(10);

            cheatsTable.add(comboTable).right().padBottom(5).row();
        }

        Table mechanicsTable = new Table();
        Label mechTitle = uiManager.createLabel("--- GAME MECHANICS ---");
        mechTitle.setColor(Color.GOLD);
        mechanicsTable.add(mechTitle).padBottom(15).row();

        for (Map.Entry<String, String> entry : model.getMechanics().entrySet()) {
            Label titleLabel = uiManager.createLabel(entry.getKey() + ": ");
            titleLabel.setColor(Color.LIME);
            titleLabel.setFontScale(0.8f);

            Label descLabel = uiManager.createLabel(entry.getValue());
            descLabel.setFontScale(0.75f);

            Table rowTable = new Table();
            rowTable.add(titleLabel).left();
            rowTable.add(descLabel).left();
            mechanicsTable.add(rowTable).left().padBottom(10).row();
        }

        mainTable.add(controlsTable).top().padRight(80);
        mainTable.add(cheatsTable).top().padTop(30).padLeft(80).row();

        mainTable.add(mechanicsTable).colspan(2).padTop(10);

        stage.addActor(mainTable);

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
        if (backgroundTexture != null) backgroundTexture.dispose();
        for (Texture tex : loadedKeyTextures) {
            tex.dispose();
        }
    }

    public Stage getStage() { return stage; }
    public TextButton getBackButton() { return backButton; }
}
