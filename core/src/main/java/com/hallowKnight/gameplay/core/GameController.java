package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.hallowKnight.gameplay.knight.KnightController;

public class GameController {
    private Game game;
    private GameModel model;
    private KnightController knightController;

    public GameController(GameModel model, Game game) {
        this.game = game;
        this.model = model;
        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game);
    }

    public void update(float delta) {
        knightController.update(delta);
    }
}
