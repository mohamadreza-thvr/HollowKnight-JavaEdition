package com.hallowKnight.Menus.achievements;

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

public class AchievementsView {
    private Stage stage;
    private Texture backgroundTexture;
    private List<Texture> loadedIcons;

    private TextButton backButton;

    public AchievementsView(AchievementsModel model) {
        stage = new Stage(new StretchViewport(1280, 720));
        loadedIcons = new ArrayList<>();


        backgroundTexture = new Texture(Gdx.files.internal("Achievements.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        UIManager uiManager = UIManager.getInstance();


        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(50);


        Table listTable = new Table();

        for (AchievementData data : model.getAchievements()) {
            Table rowTable = new Table();

            listTable.padTop(250);

            Texture iconTex = new Texture(Gdx.files.internal(data.getIconPath()));
            loadedIcons.add(iconTex);
            Image iconImage = new Image(iconTex);

            Table textTable = new Table();
            Label nameLabel = uiManager.createLabel(data.getTitle());
            Label descLabel = uiManager.createLabel(data.getDescription());
            descLabel.setFontScale(0.7f);

            //  منطق قفل بودن
            if (!data.isUnlocked()) {
                iconImage.setColor(0.3f, 0.3f, 0.3f, 1f);
                nameLabel.setColor(Color.GRAY);
                descLabel.setColor(Color.DARK_GRAY);
            }


            textTable.add(nameLabel).left().row();
            textTable.add(descLabel).left().padTop(5);

            rowTable.add(iconImage).size(64, 64).padRight(20);
            rowTable.add(textTable).expandX().left();


            listTable.add(rowTable).fillX().padBottom(20).row();
        }


        mainTable.add(listTable).expand().top().row();


        backButton = uiManager.createButton("BACK");
        mainTable.add(backButton).padTop(20).bottom();

        stage.addActor(mainTable);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        for (Texture tex : loadedIcons) {
            tex.dispose();
        }
    }

    public Stage getStage() { return stage; }
    public TextButton getBackButton() { return backButton; }
}
