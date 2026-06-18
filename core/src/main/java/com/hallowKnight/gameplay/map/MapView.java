package com.hallowKnight.gameplay.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapView {
    private OrthogonalTiledMapRenderer mapRenderer;

    public MapView(MapModel model) {
        mapRenderer = new OrthogonalTiledMapRenderer(model.getTiledMap(), 1f);
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        mapRenderer.dispose();
    }
}
