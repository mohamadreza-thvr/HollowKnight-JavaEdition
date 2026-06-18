package com.hallowKnight.gameplay.core;

import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;

public class GameModel {
    private KnightModel knightModel;
    private MapModel currentMap;

    public GameModel() {

        currentMap = new MapModel("maps/crossroad map/map.tmx");

        float startX = currentMap.getKnightSpawn().x;
        float startY = currentMap.getKnightSpawn().y;

        knightModel = new KnightModel(startX, startY);
    }

    public KnightModel getKnightModel() { return knightModel; }
    public MapModel getCurrentMap() { return currentMap; }
}
