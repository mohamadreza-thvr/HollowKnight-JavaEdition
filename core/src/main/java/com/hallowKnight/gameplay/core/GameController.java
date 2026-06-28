package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.hallowKnight.gameplay.knight.KnightController;
import com.hallowKnight.gameplay.enemy.EnemyController;

public class GameController {
    private Game game;
    private GameModel model;
    private KnightController knightController;

    private EnemyController enemyController;

    public GameController(GameModel model, Game game) {
        this.game = game;
        this.model = model;
        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game);

        this.enemyController = new EnemyController();
    }

    public void update(float delta) {
        enemyController.updateEnemies(model.getEnemies(), delta, model.getCurrentMap(), model.getKnightModel());
        knightController.update(delta, model.getEnemies());
    }
}
