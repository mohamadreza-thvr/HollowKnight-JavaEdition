package com.hallowKnight.gameplay.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MapModel {
    private TiledMap tiledMap;

    private float mapPixelWidth;
    private float mapPixelHeight;

    private List<Rectangle> solidBounds;
    private List<Rectangle> deadlyBounds;
    private Rectangle gateBounds;

    private Vector2 knightSpawn;
    private List<Vector2> enemySpawns;

    public MapModel(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);

        solidBounds = new ArrayList<>();
        deadlyBounds = new ArrayList<>();
        enemySpawns = new ArrayList<>();

        knightSpawn = new Vector2(100, 200);

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tilePixelWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        this.mapPixelWidth = mapWidth * tilePixelWidth;
        this.mapPixelHeight = mapHeight * tilePixelHeight;

        parseMapObjects();
    }

    private void parseMapObjects() {

        for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
            for (MapObject object : tiledMap.getLayers().get(i).getObjects()) {

                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    String name = object.getName();

                    if (name != null) {
                        if (name.equalsIgnoreCase("deadly")) {
                            deadlyBounds.add(rect);
                            solidBounds.add(rect);
                        } else if (name.equalsIgnoreCase("Spawn Knight")) {
                            knightSpawn.set(rect.x, rect.y);
                        } else if (name.equalsIgnoreCase("Spawn Enemy")) {
                            enemySpawns.add(new Vector2(rect.x, rect.y));
                        } else if (name.equalsIgnoreCase("Gate")) {
                            gateBounds = rect;
                        } else {
                            solidBounds.add(rect);
                        }
                    } else {

                        solidBounds.add(rect);
                    }
                }
            }
        }
    }

    public TiledMap getTiledMap() { return tiledMap; }
    public List<Rectangle> getSolidBounds() { return solidBounds; }
    public List<Rectangle> getDeadlyBounds() { return deadlyBounds; }
    public Rectangle getGateBounds() { return gateBounds; }
    public Vector2 getKnightSpawn() { return knightSpawn; }
    public List<Vector2> getEnemySpawns() { return enemySpawns; }
    public float getMapPixelWidth() { return mapPixelWidth; }
    public float getMapPixelHeight() { return mapPixelHeight; }

    public void dispose() {
        tiledMap.dispose();
    }
}
