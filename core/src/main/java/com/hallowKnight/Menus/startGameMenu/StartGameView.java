package com.hallowKnight.Menus.startGameMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hallowKnight.UIManager;

import java.util.ArrayList;
import java.util.List;

public class StartGameView {
    private Stage stage;
    private Texture backgroundTexture;

    private List<TextButton> saveButtons;
    private TextButton backButton;

    public StartGameView(List<String> slotNames) {
        stage = new Stage(new ExtendViewport(1280, 720));
        saveButtons = new ArrayList<>();

        backgroundTexture = new Texture(Gdx.files.internal("StartGameMenu.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        UIManager uiManager = UIManager.getInstance();

        Table centerTable = new Table();
        centerTable.setFillParent(true);
        centerTable.padTop(150);

        for (String slotName : slotNames) {
            TextButton btn = uiManager.createButton(slotName);
            saveButtons.add(btn);
            centerTable.add(btn).padTop(10).row();
        }
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
    public List<TextButton> getSaveButtons() { return saveButtons; }
    public TextButton getBackButton() { return backButton; }
}
