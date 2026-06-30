package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.hallowKnight.gameplay.knight.KnightController;
import com.hallowKnight.gameplay.enemy.EnemyController;
import com.hallowKnight.gameplay.npc.ZoteController;

public class GameController {
    private Game game;
    private GameModel model;
    private KnightController knightController;
    private EnemyController enemyController;

    private ZoteController zoteController;

    public GameController(GameModel model, Game game) {
        this.game = game;
        this.model = model;
        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game);
        this.enemyController = new EnemyController();

        this.zoteController = new ZoteController();
    }

    public void update(float delta) {
        // آپدیت دشمنان
        enemyController.updateEnemies(model.getEnemies(), delta, model.getCurrentMap(), model.getKnightModel());

        if (model.getZote() != null) {
            zoteController.update(model.getZote(), delta, model.getKnightModel());
        }

        knightController.update(delta, model.getEnemies(), model.getZote());
    }
}
