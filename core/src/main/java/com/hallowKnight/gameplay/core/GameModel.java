package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.gameplay.boss.BossModel;
import com.hallowKnight.gameplay.enemy.*;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;
import com.hallowKnight.gameplay.npc.ZoteModel;
import com.hallowKnight.gameplay.spells.HowlingWraithsModel;
import com.hallowKnight.gameplay.spells.VengefulSpiritModel;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private KnightModel knightModel;
    private MapModel currentMap;
    private ZoteModel zote;
    private BossModel boss;

    private List<EnemyModel> enemies;
    private List<VengefulSpiritModel> activeSpells;
    private List<HowlingWraithsModel> activeWraiths;
    private List<CrystalProjectileModel> crystalProjectiles;

    private boolean mapChanged = false;

    public GameModel() {
        knightModel = new KnightModel(0, 0);

        enemies = new ArrayList<>();
        activeSpells = new ArrayList<>();
        activeWraiths = new ArrayList<>();
        crystalProjectiles = new ArrayList<>();

        loadMap("maps/crossroad map/map.tmx");
    }

    public void loadMap(String mapPath) {
        if (currentMap != null) currentMap.dispose();

        currentMap = new MapModel(mapPath);

        float startX = currentMap.getKnightSpawn().x;
        float startY = currentMap.getKnightSpawn().y;

        knightModel.setX(startX);
        knightModel.setY(startY);
        knightModel.setVelocityX(0);
        knightModel.setVelocityY(0);

        enemies.clear();
        activeSpells.clear();
        activeWraiths.clear();
        crystalProjectiles.clear();
        boss = null;

        for (Vector2 pos : currentMap.getCrawlidSpawns())
            enemies.add(new CrawlidModel(pos.x, pos.y));

        for (Vector2 pos : currentMap.getMosquitoSpawns())
            enemies.add(new MosquitoModel(pos.x, pos.y));

        for (Vector2 pos : currentMap.getHuskSpawns())
            enemies.add(new HuskModel(pos.x, pos.y));

        for (Vector2 pos : currentMap.getCrawlerSpawns())
            enemies.add(new CrystalCrawlerModel(pos.x, pos.y));

        for (Vector2 pos : currentMap.getHunterSpawns())
            enemies.add(new CrystalHunterModel(pos.x, pos.y));

        for (Vector2 pos : currentMap.getGuardianSpawns())
            enemies.add(new CrystalGuardianModel(pos.x, pos.y));

        if (currentMap.getZoteSpawn() != null)
            zote = new ZoteModel(currentMap.getZoteSpawn().x, currentMap.getZoteSpawn().y);
        else
            zote = null;

        if (currentMap.getBossSpawn() != null)
            boss = new BossModel(currentMap.getBossSpawn().x, currentMap.getBossSpawn().y);

        mapChanged = true;
    }

    public KnightModel getKnightModel() { return knightModel; }
    public MapModel getCurrentMap() { return currentMap; }
    public List<EnemyModel> getEnemies() { return enemies; }
    public ZoteModel getZote() { return zote; }
    public BossModel getBoss() { return boss; }
    public List<VengefulSpiritModel> getActiveSpells() { return activeSpells; }
    public List<HowlingWraithsModel> getActiveWraiths() { return activeWraiths; }
    public List<CrystalProjectileModel> getCrystalProjectiles() { return crystalProjectiles; }

    public boolean isMapChanged() { return mapChanged; }
    public void setMapChanged(boolean mapChanged) { this.mapChanged = mapChanged; }
}
