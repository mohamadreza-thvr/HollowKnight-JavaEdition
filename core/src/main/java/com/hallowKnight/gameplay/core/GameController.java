package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.gameplay.knight.KnightController;
import com.hallowKnight.gameplay.knight.KnightModel;
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

        Rectangle knightBounds = model.getKnightModel().getBounds();

        if (model.getCurrentMap().getGateBounds() != null && knightBounds.overlaps(model.getCurrentMap().getGateBounds())) {
            transitionToMap("maps/crystal map/untitled.tmx");
            return;
        }
        else if (model.getCurrentMap().getBossGateBounds() != null && knightBounds.overlaps(model.getCurrentMap().getBossGateBounds())) {
            transitionToMap("maps/crossroad map/map.tmx");
            return;
        }

        enemyController.updateEnemies(model.getEnemies(), delta, model.getCurrentMap(), model.getKnightModel());

        if (model.getZote() != null) {
            zoteController.update(model.getZote(), delta, model.getKnightModel());
        }

        knightController.update(delta, model.getEnemies(), model.getZote(), model.getActiveSpells(), model.getActiveWraiths());

        boolean hasVoidHeart = model.getKnightModel().isCharmEquipped(KnightModel.Charm.VOID_HEART);

        for (int i = model.getActiveSpells().size() - 1; i >= 0; i--) {
            VengefulSpiritModel spell = model.getActiveSpells().get(i);
            spell.update(delta);

            for (EnemyModel enemy : model.getEnemies()) {
                if (!enemy.isDead() && spell.getBounds().overlaps(enemy.getBounds()) && !spell.hasHit(enemy)) {
                    int spellDamage = hasVoidHeart ? 3 : 2;
                    enemy.takeDamage(spellDamage, spell.getX(), false);
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

                    int baseDamage = 1;
                    float finalDamage = hasVoidHeart ? baseDamage * 1.5f : baseDamage;
                    int calculatedDamage = Math.max(1, Math.round(finalDamage));

                    enemy.takeDamage(calculatedDamage, wraith.getX() + (wraith.getBounds().width / 2f), false);
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

    private void transitionToMap(String newMapPath) {
        model.loadMap(newMapPath);
        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game);
    }
}
