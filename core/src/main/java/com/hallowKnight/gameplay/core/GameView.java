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
import com.hallowKnight.gameplay.boss.BossModel;
import com.hallowKnight.gameplay.boss.BossView;
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


    private KnightView knightView;
    private ZoteView zoteView = new ZoteView();
    private BossView bossView;

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

        this.knightView = new KnightView(model.getKnightModel());
        this.enemyView = new EnemyView();
        this.bossView  = new BossView();
        this.spellView = new VengefulSpiritView(model.getKnightModel());
        this.wraithsView = new HowlingWraithsView(model.getKnightModel());
    }

    public void render(float delta) {
        if (model.isMapChanged()) {
            if (mapView != null) mapView.dispose();
            mapView = new MapView(model.getCurrentMap());
            model.setMapChanged(false);
        }

        float cameraHalfWidth  = camera.viewportWidth  / 2f;
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
            maxX = Math.max(minX, currentZone.x + currentZone.width  - cameraHalfWidth);
            minY = currentZone.y + cameraHalfHeight;
            maxY = Math.max(minY, currentZone.y + currentZone.height - cameraHalfHeight);
        } else {
            float mapWidth  = model.getCurrentMap().getMapPixelWidth();
            float mapHeight = model.getCurrentMap().getMapPixelHeight();
            minX = cameraHalfWidth;
            maxX = Math.max(minX, mapWidth  - cameraHalfWidth);
            minY = cameraHalfHeight;
            maxY = Math.max(minY, mapHeight - cameraHalfHeight);
        }

        float targetY = knightY + 70f;
        float camX = MathUtils.clamp(knightX, minX, maxX);
        float camY = MathUtils.clamp(targetY,  minY, maxY);

        BossModel boss = model.getBoss();
        if (boss != null && boss.getCameraShakeTimer() > 0) {
            float intensity = boss.getCameraShakeIntensity();
            camX += MathUtils.random(-intensity, intensity);
            camY += MathUtils.random(-intensity, intensity);
        }

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
        enemyView.render(batch, delta, model.getEnemies(), model.getCrystalProjectiles());
        knightView.render(batch, delta);

        spellView.render(batch, model.getActiveSpells());
        wraithsView.render(batch, model.getActiveWraiths());

        zoteView.renderWorld(batch, delta, model.getZote());

        if (boss != null) {
            bossView.render(batch, boss, delta);
        }

        batch.end();



        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        hudView.render(batch, delta);
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        background.dispose();
        batch.dispose();
        knightView.dispose();
        zoteView.dispose();
        if (enemyView   != null) enemyView.dispose();
        if (spellView   != null) spellView.dispose();
        if (bossView    != null) bossView.dispose();
    }
}
