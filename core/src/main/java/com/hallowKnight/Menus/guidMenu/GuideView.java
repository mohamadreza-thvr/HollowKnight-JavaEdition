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

        Table table = new Table();
        table.setFillParent(true);
        table.padTop(250);

        for (Map.Entry<String, String> entry : model.getControls().entrySet()) {
            Label actionLabel = uiManager.createLabel(entry.getKey());
            actionLabel.setFontScale(1.2f);

            Texture keyTex = new Texture(Gdx.files.internal(entry.getValue()));
            loadedKeyTextures.add(keyTex);
            Image keyImage = new Image(keyTex);

            table.add(actionLabel).left().padLeft(350);

            Label dots = uiManager.createLabel(" ........................ ");
            dots.setColor(Color.DARK_GRAY);
            table.add(dots).expandX().center().padRight(78);

            table.add(keyImage).size(50, 45).right().padRight(350).row();
        }

        stage.addActor(table);

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
