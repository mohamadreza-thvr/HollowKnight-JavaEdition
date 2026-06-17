package com.hallowKnight.in_game_menus.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.UIManager;

public class InventoryView {
    private Stage stage;
    private Texture darkOverlay;

    public InventoryView() {
        stage = new Stage(new StretchViewport(1280, 720));
        UIManager uiManager = UIManager.getInstance();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.1f, 0.1f, 0.2f, 0.9f)); // سرمه‌ای تاریک
        pixmap.fill();
        darkOverlay = new Texture(pixmap);
        pixmap.dispose();

        Image backgroundImage = new Image(darkOverlay);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);

        Label title = uiManager.createLabel("INVENTORY & CHARMS");
        title.setFontScale(1.5f);

        Label info = uiManager.createLabel("Press 'I' again to return");
        info.setColor(Color.GRAY);

        table.add(title).padBottom(30).row();
        table.add(info).row();

        stage.addActor(table);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() { return stage; }

    public void dispose() {
        stage.dispose();
        darkOverlay.dispose();
    }
}
