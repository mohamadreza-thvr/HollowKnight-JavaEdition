package com.hallowKnight.gameplay.core;

import com.hallowKnight.gameplay.knight.KnightModel;

public class GameModel {
    private KnightModel knightModel;

    public GameModel() {
        knightModel = new KnightModel(640, 400);
    }

    public KnightModel getKnightModel() {
        return knightModel;
    }
}
