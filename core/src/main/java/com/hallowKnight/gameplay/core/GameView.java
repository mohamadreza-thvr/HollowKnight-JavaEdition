package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hallowKnight.gameplay.knight.KnightView;

public class GameView {
    private GameModel model;
    private OrthographicCamera camera;
    private Viewport viewport;
    private KnightView knightView;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GameView(GameModel model) {
        this.model = model;
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1280, 720, camera);

        this.knightView = new KnightView(model.getKnightModel());
    }

    public void render() {
        camera.position.set(model.getKnightModel().getX(), model.getKnightModel().getY(), 0);
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(-5000, 0, 10000, 100);

        shapeRenderer.setColor(Color.LIGHT_GRAY);
        for (int i = -2000; i < 2000; i += 200) {
            shapeRenderer.rect(i, 100, 20, 50);
        }

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(model.getKnightModel().getX(), model.getKnightModel().getY(),
            model.getKnightModel().getBounds().width,
            model.getKnightModel().getBounds().height);

        shapeRenderer.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        knightView.dispose();
    }
}
