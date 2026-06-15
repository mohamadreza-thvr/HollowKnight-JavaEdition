package com.hallowKnight.gameplay.core;

import com.hallowKnight.gameplay.knight.KnightController;

public class GameController {
    private GameModel model;
    private KnightController knightController;

    public GameController(GameModel model) {
        this.model = model;
        this.knightController = new KnightController(model.getKnightModel());
    }

    public void update(float delta) {
        knightController.update(delta);
    }
}
