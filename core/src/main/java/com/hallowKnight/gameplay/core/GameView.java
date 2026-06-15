package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hallowKnight.gameplay.knight.KnightView;

public class GameView {
    private GameModel model;
    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private KnightView knightView;

    public GameView(GameModel model) {
        this.model = model;
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1280, 720, camera);

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        this.knightView = new KnightView(model.getKnightModel());
    }

    public void render(float delta) {
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
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        knightView.render(batch, delta);
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        knightView.dispose();
    }
}
