package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class KnightView {
    private KnightModel model;
    private ShapeRenderer shapeRenderer;

    public KnightView(KnightModel model) {
        this.model = model;
        this.shapeRenderer = new ShapeRenderer();
    }


    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(model.getX(), model.getY(), model.getBounds().width, model.getBounds().height);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
