package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hallowKnight.gameplay.npc.ZoteView;
import com.hallowKnight.gameplay.hud.HudView;
import com.hallowKnight.gameplay.knight.KnightView;
import com.hallowKnight.gameplay.map.MapView;
import com.hallowKnight.gameplay.enemy.EnemyView;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import com.hallowKnight.gameplay.spells.HowlingWraithsView;
import com.hallowKnight.gameplay.spells.VengefulSpiritView;

public class GameView {
    private GameModel model;
    private OrthographicCamera camera;
    private Viewport viewport;
    private MapView mapView;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private KnightView knightView;
    private ZoteView zoteView = new ZoteView();

    private EnemyView enemyView;

    private com.badlogic.gdx.graphics.Texture background;
    private HudView hudView;
    private VengefulSpiritView spellView;
    private HowlingWraithsView wraithsView;

    public GameView(GameModel model) {
        this.model = model;
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1280, 720, camera);
        this.mapView = new MapView(model.getCurrentMap());
        this.background = new com.badlogic.gdx.graphics.Texture("bg2.png");

        this.hudView = new HudView(model.getKnightModel());

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        this.knightView = new KnightView(model.getKnightModel());
        this.enemyView = new EnemyView();
        this.spellView = new VengefulSpiritView(model.getKnightModel());
        this.wraithsView = new HowlingWraithsView();
    }

    public void render(float delta) {

        float cameraHalfWidth = camera.viewportWidth / 2f;
        float cameraHalfHeight = camera.viewportHeight / 2f;

        float knightX = model.getKnightModel().getX();
        float knightY = model.getKnightModel().getY();

        Rectangle currentZone = null;

        if (model.getCurrentMap().getCameraZones() != null) {
            for (Rectangle zone : model.getCurrentMap().getCameraZones()) {
                if (zone.contains(knightX, knightY)) {
                    currentZone = zone;
                    break;
                }
            }
        }

        float minX, maxX, minY, maxY;

        if (currentZone != null) {
            minX = currentZone.x + cameraHalfWidth;
            maxX = Math.max(minX, currentZone.x + currentZone.width - cameraHalfWidth);
            minY = currentZone.y + cameraHalfHeight;
            maxY = Math.max(minY, currentZone.y + currentZone.height - cameraHalfHeight);
        } else {
            float mapWidth = model.getCurrentMap().getMapPixelWidth();
            float mapHeight = model.getCurrentMap().getMapPixelHeight();
            minX = cameraHalfWidth;
            maxX = Math.max(minX, mapWidth - cameraHalfWidth);
            minY = cameraHalfHeight;
            maxY = Math.max(minY, mapHeight - cameraHalfHeight);
        }

        float targetY = knightY + 70f;
        float camX = MathUtils.clamp(knightX, minX, maxX);
        float camY = MathUtils.clamp(targetY, minY, maxY);

        camera.position.set(camX, camY, 0);
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
        enemyView.render(batch, delta, model.getEnemies());
        knightView.render(batch, delta);

        spellView.render(batch, model.getActiveSpells());
        wraithsView.render(batch, model.getActiveWraiths());

        zoteView.renderWorld(batch, delta, model.getZote());
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        for (Rectangle solid : model.getCurrentMap().getSolidBounds()) {
            shapeRenderer.rect(solid.x, solid.y, solid.width, solid.height);
        }

        shapeRenderer.setColor(Color.YELLOW);
        for (Rectangle deadly : model.getCurrentMap().getDeadlyBounds()) {
            shapeRenderer.rect(deadly.x, deadly.y, deadly.width, deadly.height);
        }

        shapeRenderer.setColor(Color.PURPLE);
        for (EnemyModel enemy : model.getEnemies()) {
            Rectangle enemyRect = enemy.getBounds();
            shapeRenderer.rect(enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
        }

        shapeRenderer.setColor(Color.GREEN);
        Rectangle knightRect = model.getKnightModel().getBounds();
        shapeRenderer.rect(knightRect.x, knightRect.y, knightRect.width, knightRect.height);

        if (model.getZote() != null) {
            shapeRenderer.setColor(Color.CYAN);
            Rectangle zoteRect = model.getZote().getBounds();
            shapeRenderer.rect(zoteRect.x, zoteRect.y, zoteRect.width, zoteRect.height);
        }

        if (model.getCurrentMap().getCameraZones() != null) {
            shapeRenderer.setColor(Color.NAVY);
            for (Rectangle zone : model.getCurrentMap().getCameraZones()) {
                shapeRenderer.rect(zone.x, zone.y, zone.width, zone.height);
            }
        }

        shapeRenderer.end();

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.begin();
        hudView.render(batch, delta);
        batch.end();

        zoteView.renderUI(batch, shapeRenderer, model.getZote());
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        background.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        knightView.dispose();
        zoteView.dispose();

        if (enemyView != null) enemyView.dispose();
        if (spellView != null) spellView.dispose();
    }
}
