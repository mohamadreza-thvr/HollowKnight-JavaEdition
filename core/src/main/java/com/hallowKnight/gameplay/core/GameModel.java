package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.gameplay.enemy.*;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;
import com.hallowKnight.gameplay.npc.ZoteModel;
import com.hallowKnight.gameplay.spells.VengefulSpiritModel;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private KnightModel knightModel;
    private MapModel currentMap;
    private ZoteModel zote;

    private List<EnemyModel> enemies;
    private List<VengefulSpiritModel> activeSpells;

    public GameModel() {

        currentMap = new MapModel("maps/crossroad map/map.tmx");

        float startX = currentMap.getKnightSpawn().x;
        float startY = currentMap.getKnightSpawn().y;

        knightModel = new KnightModel(startX, startY);

        enemies = new ArrayList<>();
        activeSpells = new ArrayList<>();

        for (Vector2 pos : currentMap.getCrawlidSpawns()) {
            enemies.add(new CrawlidModel(pos.x, pos.y));
        }

        for (Vector2 pos : currentMap.getMosquitoSpawns()) {
            enemies.add(new MosquitoModel(pos.x, pos.y));
        }

        for (Vector2 pos : currentMap.getHuskSpawns()) {
            enemies.add(new HuskModel(pos.x, pos.y));
        }
        if (currentMap.getZoteSpawn() != null) {
            zote = new ZoteModel(currentMap.getZoteSpawn().x, currentMap.getZoteSpawn().y);
        }
    }

    public KnightModel getKnightModel() { return knightModel; }
    public MapModel getCurrentMap() { return currentMap; }

    public List<EnemyModel> getEnemies() { return enemies; }
    public ZoteModel getZote() { return zote; }
    public List<VengefulSpiritModel> getActiveSpells() { return activeSpells; }
}
