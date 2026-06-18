package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hallowKnight.gameplay.knight.KnightView;
import com.hallowKnight.gameplay.map.MapView;

public class GameView {
    private GameModel model;
    private OrthographicCamera camera;
    private Viewport viewport;
    private MapView mapView;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private KnightView knightView;

    private com.badlogic.gdx.graphics.Texture background;


    public GameView(GameModel model) {
        this.model = model;
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1280, 720, camera);
        this.mapView = new MapView(model.getCurrentMap());
        this.background = new com.badlogic.gdx.graphics.Texture("bg.png");

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        this.knightView = new KnightView(model.getKnightModel());
    }

    public void render(float delta) {

        float cameraHalfWidth = camera.viewportWidth / 2f;
        float cameraHalfHeight = camera.viewportHeight / 2f;

        float mapWidth = model.getCurrentMap().getMapPixelWidth();
        float mapHeight = model.getCurrentMap().getMapPixelHeight();

        float minX = cameraHalfWidth;
        float maxX = Math.max(minX, mapWidth - cameraHalfWidth);

        float minY = cameraHalfHeight;
        float maxY = Math.max(minY, mapHeight - cameraHalfHeight);

        float knightX = model.getKnightModel().getX();
        float knightY = model.getKnightModel().getY();

        float camX = MathUtils.clamp(knightX, minX, maxX);
        float camY = MathUtils.clamp(knightY, minY, maxY);

        camera.position.set(camX, camY + 70f, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background,
            camera.position.x - cameraHalfWidth,
            camera.position.y - cameraHalfHeight,
            camera.viewportWidth,
            camera.viewportHeight);
        batch.end();

        mapView.render(camera);

        batch.begin();
        knightView.render(batch, delta);
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        background.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        knightView.dispose();
    }
}
