package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import com.hallowKnight.gameplay.enemy.CrawlidModel;
import com.hallowKnight.gameplay.enemy.MosquitoModel;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private KnightModel knightModel;
    private MapModel currentMap;

    private List<EnemyModel> enemies;

    public GameModel() {

        currentMap = new MapModel("maps/crossroad map/map.tmx");

        float startX = currentMap.getKnightSpawn().x;
        float startY = currentMap.getKnightSpawn().y;

        knightModel = new KnightModel(startX, startY);

        enemies = new ArrayList<>();

        for (Vector2 pos : currentMap.getCrawlidSpawns()) {
            enemies.add(new CrawlidModel(pos.x, pos.y));
        }

        for (Vector2 pos : currentMap.getMosquitoSpawns()) {
            enemies.add(new MosquitoModel(pos.x, pos.y));
        }
    }

    public KnightModel getKnightModel() { return knightModel; }
    public MapModel getCurrentMap() { return currentMap; }

    public List<EnemyModel> getEnemies() { return enemies; }
}
