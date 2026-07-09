package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.gameplay.boss.BossController;
import com.hallowKnight.gameplay.boss.BossModel;
import com.hallowKnight.gameplay.knight.KnightController;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.enemy.EnemyController;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import com.hallowKnight.gameplay.npc.ZoteController;
import com.hallowKnight.gameplay.spells.HowlingWraithsModel;
import com.hallowKnight.gameplay.spells.VengefulSpiritModel;
import com.hallowKnight.gameplay.core.AudioManager;
import com.hallowKnight.Menus.endGameMenu.EndGameScreen;

public class GameController {
    private Game game;
    private GameModel model;
    private KnightController knightController;
    private EnemyController enemyController;
    private ZoteController zoteController;
    private BossController bossController;
    private int slotIndex;

    private final String CROSSROAD_MAP = "maps/crossroad map/map.tmx";
    private final String CRYSTAL_MAP   = "maps/crystal map/untitled.tmx";
    private final String BOSS_MAP      = "maps/boss map/boss.tmx";

    private String currentMapPath;

    public GameController(GameModel model, Game game, int slotIndex) {
        this.game = game;
        this.model = model;
        this.slotIndex = slotIndex;

        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game, slotIndex);
        this.enemyController  = new EnemyController();
        this.zoteController   = new ZoteController();
        this.bossController   = new BossController();

        this.currentMapPath = model.getCurrentMapPath();
        playMapMusic(currentMapPath);
    }

    public void update(float delta) {
        Rectangle knightBounds = model.getKnightModel().getBounds();

        if (model.getKnightModel().isRequestBossTeleport()) {
            model.getKnightModel().setRequestBossTeleport(false);

            if (!currentMapPath.equals(BOSS_MAP)) {
                transitionToMap(BOSS_MAP);
            }

            if (model.getCurrentMap().getBossSpawn() != null) {
                model.getKnightModel().setX(model.getCurrentMap().getBossSpawn().x - 200f);
                model.getKnightModel().setY(model.getCurrentMap().getBossSpawn().y);
                model.getKnightModel().updateBounds();
            }
        }

        if (model.getCurrentMap().getGateBounds() != null
            && knightBounds.overlaps(model.getCurrentMap().getGateBounds())) {
            if (currentMapPath.equals(CROSSROAD_MAP)) {
                transitionToMap(CRYSTAL_MAP);
            } else if (currentMapPath.equals(CRYSTAL_MAP)) {
                transitionToMap(CROSSROAD_MAP);
            }
            return;
        }

        if (model.getCurrentMap().getBossGateBounds() != null
            && knightBounds.overlaps(model.getCurrentMap().getBossGateBounds())) {
            if (currentMapPath.equals(CRYSTAL_MAP)) {
                transitionToMap(BOSS_MAP);
            } else if (currentMapPath.equals(BOSS_MAP)) {
                model.getKnightModel().setX(model.getKnightModel().getX() + 20f);
                model.getKnightModel().updateBounds();
            }
            return;
        }

        enemyController.updateEnemies(
            model.getEnemies(),
            model.getCrystalProjectiles(),
            delta,
            model.getCurrentMap(),
            model.getKnightModel()
        );

        if (model.getZote() != null) {
            zoteController.update(model.getZote(), delta, model.getKnightModel());
        }

        BossModel boss = model.getBoss();
        if (boss != null) {
            bossController.update(boss, delta, model.getKnightModel(), model.getCurrentMap());

            if (boss.isDead() && currentMapPath.equals(BOSS_MAP)) {
                AchievementManager.getInstance().unlockAchievement("Completion");
                if (AchievementManager.getInstance().totalPlayTime < 7200) {
                    AchievementManager.getInstance().unlockAchievement("Speedrun");
                }

                game.setScreen(new EndGameScreen(game, model.getKnightModel(), slotIndex));
                return;
            }

            Rectangle hitbox = boss.getAttackHitbox();
            if (hitbox.width > 0 && hitbox.overlaps(model.getKnightModel().getBounds())) {
                model.getKnightModel().takeDamage(1);
            }

            boolean hasVoidHeart = model.getKnightModel().isCharmEquipped(KnightModel.Charm.VOID_HEART);

            for (int i = model.getActiveSpells().size() - 1; i >= 0; i--) {
                VengefulSpiritModel spell = model.getActiveSpells().get(i);
                if (!boss.isDead() && spell.getBounds().overlaps(boss.getBounds()) && !boss.isSpellHit()) {
                    int spellDamage = hasVoidHeart ? 3 : 2;
                    boss.takeDamage(spellDamage);
                    boss.setSpellHit(true);
                    spell.setActive(false);
                }
            }

            for (int i = model.getActiveWraiths().size() - 1; i >= 0; i--) {
                HowlingWraithsModel wraith = model.getActiveWraiths().get(i);
                if (!boss.isDead() && wraith.getBounds().overlaps(boss.getBounds()) && !boss.isWraithHit()) {
                    int baseDamage = hasVoidHeart ? 2 : 1;
                    boss.takeDamage(baseDamage);
                    boss.setWraithHit(true);
                }
            }
        }

        knightController.update(
            delta,
            model.getEnemies(),
            model.getZote(),
            model.getBoss(),
            model.getActiveSpells(),
            model.getActiveWraiths()
        );

        boolean hasVoidHeart = model.getKnightModel().isCharmEquipped(KnightModel.Charm.VOID_HEART);

        for (int i = model.getActiveSpells().size() - 1; i >= 0; i--) {
            VengefulSpiritModel spell = model.getActiveSpells().get(i);
            spell.update(delta);

            for (EnemyModel enemy : model.getEnemies()) {
                if (!enemy.isDead() && spell.getBounds().overlaps(enemy.getBounds()) && !spell.hasHit(enemy)) {
                    boolean wasEnemyDead = enemy.isDead();
                    int spellDamage = hasVoidHeart ? 3 : 2;
                    enemy.takeDamage(spellDamage, spell.getX(), false);
                    spell.addHitEnemy(enemy);

                    if (!wasEnemyDead && enemy.isDead()) {
                        AchievementManager.getInstance().registerEnemyKill(enemy.getClass().getSimpleName());
                        model.getKnightModel().setTotalKills(model.getKnightModel().getTotalKills() + 1);
                    }
                }
            }

            if (model.getZote() != null && spell.getBounds().overlaps(model.getZote().getBounds())) {
                model.getZote().takeDamage();
                spell.setActive(false);
            }
            if (!spell.isActive()) model.getActiveSpells().remove(i);
        }

        for (int i = model.getActiveWraiths().size() - 1; i >= 0; i--) {
            HowlingWraithsModel wraith = model.getActiveWraiths().get(i);
            wraith.update(delta);

            for (EnemyModel enemy : model.getEnemies()) {
                if (!enemy.isDead() && wraith.getBounds().overlaps(enemy.getBounds()) && !wraith.hasHit(enemy)) {
                    boolean wasEnemyDead = enemy.isDead();
                    int baseDamage = hasVoidHeart ? 2 : 1;
                    enemy.takeDamage(baseDamage, wraith.getX() + wraith.getBounds().width / 2f, false);
                    wraith.addHitEnemy(enemy);

                    if (!wasEnemyDead && enemy.isDead()) {
                        AchievementManager.getInstance().registerEnemyKill(enemy.getClass().getSimpleName());
                        model.getKnightModel().setTotalKills(model.getKnightModel().getTotalKills() + 1);
                    }
                }
            }

            if (model.getZote() != null && wraith.getBounds().overlaps(model.getZote().getBounds())) {
                model.getZote().takeDamage();
                wraith.setActive(false);
            }
            if (!wraith.isActive()) model.getActiveWraiths().remove(i);
        }
    }

    private void transitionToMap(String newMapPath) {
        model.loadMap(newMapPath);
        this.currentMapPath = newMapPath;
        this.knightController = new KnightController(model.getKnightModel(), model.getCurrentMap(), game, slotIndex);

        playMapMusic(newMapPath);
    }

    private void playMapMusic(String mapPath) {
        if (mapPath.equals(CROSSROAD_MAP)) {
            AudioManager.playMusic("audio/music/Crossroads.mp3");
        } else if (mapPath.equals(CRYSTAL_MAP)) {
            AudioManager.playMusic("audio/music/Crystal Peak.mp3");
        } else if (mapPath.equals(BOSS_MAP)) {
            AudioManager.playMusic("audio/music/boss_fight.wav");
        }
    }
}
