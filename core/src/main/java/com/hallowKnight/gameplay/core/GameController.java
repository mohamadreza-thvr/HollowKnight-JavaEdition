package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.hallowKnight.gameplay.knight.KnightController;
import com.hallowKnight.gameplay.enemy.EnemyController;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import com.hallowKnight.gameplay.npc.ZoteController;
import com.hallowKnight.gameplay.spells.HowlingWraithsModel;
import com.hallowKnight.gameplay.spells.VengefulSpiritModel;

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
        enemyController.updateEnemies(model.getEnemies(), delta, model.getCurrentMap(), model.getKnightModel());

        if (model.getZote() != null) {
            zoteController.update(model.getZote(), delta, model.getKnightModel());
        }

        knightController.update(delta, model.getEnemies(), model.getZote(), model.getActiveSpells(), model.getActiveWraiths());

        for (int i = model.getActiveSpells().size() - 1; i >= 0; i--) {
            VengefulSpiritModel spell = model.getActiveSpells().get(i);

            spell.update(delta);

            for (EnemyModel enemy : model.getEnemies()) {
                if (!enemy.isDead() && spell.getBounds().overlaps(enemy.getBounds()) && !spell.hasHit(enemy)) {
                    enemy.takeDamage(2, spell.getX());
                    spell.addHitEnemy(enemy);
                }
            }

            if (model.getZote() != null && spell.getBounds().overlaps(model.getZote().getBounds())) {
                model.getZote().takeDamage();
                spell.setActive(false);
            }
            if (!spell.isActive()) {
                model.getActiveSpells().remove(i);
            }
        }

        for (int i = model.getActiveWraiths().size() - 1; i >= 0; i--) {
            HowlingWraithsModel wraith = model.getActiveWraiths().get(i);
            wraith.update(delta);

            for (EnemyModel enemy : model.getEnemies()) {
                if (!enemy.isDead() && wraith.getBounds().overlaps(enemy.getBounds()) && !wraith.hasHit(enemy)) {
                    enemy.takeDamage(1, wraith.getX() + (wraith.getBounds().width / 2f));
                    wraith.addHitEnemy(enemy);
                }
            }

            if (model.getZote() != null && wraith.getBounds().overlaps(model.getZote().getBounds())) {
                model.getZote().takeDamage();
                wraith.setActive(false);
            }

            if (!wraith.isActive()) {
                model.getActiveWraiths().remove(i);
            }
        }
    }

}
