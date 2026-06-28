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

    private List<Vector2> crawlidSpawns;
    private List<Vector2> mosquitoSpawns;
    private List<Vector2> huskSpawns;

    public MapModel(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);

        solidBounds = new ArrayList<>();
        deadlyBounds = new ArrayList<>();

        crawlidSpawns = new ArrayList<>();
        mosquitoSpawns = new ArrayList<>();
        huskSpawns = new ArrayList<>();


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

                String name = object.getName();
                String cleanName = (name != null) ? name.trim().replaceAll("\\s+", " ") : null;

                Float objX = object.getProperties().get("x", Float.class);
                Float objY = object.getProperties().get("y", Float.class);


                if (cleanName != null && cleanName.equalsIgnoreCase("Spawn Knight")) {
                    knightSpawn.set(objX, objY);
                }
                else if (cleanName != null && cleanName.equalsIgnoreCase("Spawn Crawlid")) {
                    crawlidSpawns.add(new Vector2(objX, objY));
                }
                else if (cleanName != null && cleanName.equalsIgnoreCase("Spawn Mosquito")) {
                    mosquitoSpawns.add(new Vector2(objX, objY));
                } else if (cleanName != null && cleanName.equalsIgnoreCase("Spawn Husk")) {
                    huskSpawns.add(new Vector2(objX, objY));
                }

                else if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();

                    if (cleanName != null) {
                        if (cleanName.equalsIgnoreCase("deadly")) {
                            deadlyBounds.add(rect);
                            solidBounds.add(rect);
                        } else if (cleanName.equalsIgnoreCase("Gate")) {
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

    public List<Vector2> getCrawlidSpawns() { return crawlidSpawns; }
    public List<Vector2> getMosquitoSpawns() { return mosquitoSpawns; }
    public List<Vector2> getHuskSpawns() { return huskSpawns; }


    public float getMapPixelWidth() { return mapPixelWidth; }
    public float getMapPixelHeight() { return mapPixelHeight; }

    public void dispose() {
        tiledMap.dispose();
    }
}
