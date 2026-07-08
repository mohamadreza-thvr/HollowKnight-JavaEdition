package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.core.GameScreen;
import com.hallowKnight.gameplay.map.MapModel;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import com.hallowKnight.gameplay.npc.ZoteModel;
import com.hallowKnight.gameplay.boss.BossModel;
import com.hallowKnight.gameplay.spells.HowlingWraithsModel;
import com.hallowKnight.gameplay.spells.VengefulSpiritModel;
import com.hallowKnight.gameplay.core.AudioManager;
import com.hallowKnight.gameplay.core.AchievementManager;

import java.util.List;

public class KnightController {
    private Game game;
    private KnightModel model;
    private MapModel mapModel;
    private int slotIndex;

    public KnightController(KnightModel model, MapModel mapModel, Game game, int slotIndex) {
        this.model = model;
        this.mapModel = mapModel;
        this.game = game;
        this.slotIndex = slotIndex;
    }

    public void update(float delta, List<EnemyModel> enemies, ZoteModel zote, BossModel boss, List<VengefulSpiritModel> activeSpells, List<HowlingWraithsModel> activeWraiths) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }

        if (model.isDead()) {
            model.setDeathTimer(model.getDeathTimer() + delta);
            model.setVelocityX(0);
            applyPhysics(delta, mapModel);

            if (model.getDeathTimer() > 1.4f) {
                game.setScreen(new GameScreen(game, slotIndex));
            }
            return;
        }

        if (model.getDashCooldownTimer() > 0) {
            model.setDashCooldownTimer(model.getDashCooldownTimer() - delta);
        }

        if (model.isAttacking()) {
            model.setAttackTimer(model.getAttackTimer() + delta);

            float attackLimit = model.isCharmEquipped(KnightModel.Charm.QUICK_SLASH) ? 0.25f : 0.4f;

            if (model.getAttackTimer() >= attackLimit) {
                model.setAttacking(false);
            }
        }

        model.setWasGrounded(model.isGrounded());

        handleInput(delta, enemies, zote, boss, activeSpells, activeWraiths);
        applyPhysics(delta, mapModel);

        if (!model.isDead() && !model.isDashing()) {
            for (EnemyModel enemy : enemies) {
                if (!enemy.isDead() && model.getBounds().overlaps(enemy.getBounds())) {
                    if (!model.isInvulnerable()) AudioManager.playTakeDamage();
                    model.takeDamage(1);
                }
            }
            if (boss != null && !boss.isDead() && model.getBounds().overlaps(boss.getBounds())) {
                if (!model.isInvulnerable()) AudioManager.playTakeDamage();
                model.takeDamage(1);
            }
        }

        if (model.isGrounded() || model.isWallSliding()) {
            model.setCanDoubleJump(true);
            if (model.getDashCooldownTimer() <= 0) {
                model.setCanDash(true);
            }
            model.setDoubleJumping(false);
        }

        if (model.isInvulnerable()) {
            model.setInvulnerabilityTimer(model.getInvulnerabilityTimer() - delta);
            if (model.getInvulnerabilityTimer() <= 0) {
                model.setInvulnerable(false);
            }
        }

        if (model.isGrounded() && model.getVelocityY() == 0) {
            boolean nearHazard = false;
            for (Rectangle spike : mapModel.getDeadlyBounds()) {
                if (model.getBounds().overlaps(spike)) {
                    nearHazard = true;
                    break;
                }
            }
            if (!nearHazard) {
                model.setSafePosition(model.getX(), model.getY());
            }
        }

        if (!model.wasGrounded() && model.isGrounded()) {
            model.setLanding(true);
            model.setLandingTimer(0.4f);
        }

        if (model.isLanding()) {
            model.setLandingTimer(model.getLandingTimer() - delta);
            if (model.getLandingTimer() <= 0) {
                model.setLanding(false);
            }
        }
    }

    private void handleInput(float delta, List<EnemyModel> enemies, ZoteModel zote, BossModel boss, List<VengefulSpiritModel> activeSpells, List<HowlingWraithsModel> activeWraiths) {

        if (zote != null && zote.isTalking()) {
            model.setVelocityX(0);
            if (model.isDashing()) {
                model.setDashing(false);
            }
            model.setAttacking(false);
            return;
        }

        if (model.isCasting()) {
            model.setCastTimer(model.getCastTimer() - delta);
            model.setVelocityX(0);
            model.setVelocityY(0);
            if (model.getCastTimer() <= 0) {
                model.setCasting(false);
            }
            return;
        }

        if (model.isDashing()) {
            model.setDashTimer(model.getDashTimer() - delta);
            model.setVelocityY(0);

            if (model.getDashTimer() <= 0) {
                model.setDashing(false);
                model.setVelocityX(0);

                float cooldown = model.isCharmEquipped(KnightModel.Charm.DASHMASTER) ? 0.5f : 1f;
                model.setDashCooldownTimer(cooldown);
            }

            if (model.isCharmEquipped(KnightModel.Charm.SHARP_SHADOW)) {
                for (EnemyModel enemy : enemies) {
                    if (!enemy.isDead() && model.getBounds().overlaps(enemy.getBounds())) {
                        enemy.takeDamage(1, model.getX(), false);
                    }
                }

                if (boss != null && !boss.isDead() && model.getBounds().overlaps(boss.getBounds())) {
                    boss.takeDamage(1);
                }
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S) && model.getCurrentSoul() >= 33) {
            model.setCurrentSoul(model.getCurrentSoul() - 33);
            model.setCasting(true);

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                model.setCastingUp(true);
                model.setCastTimer(0.65f);
                activeWraiths.add(new HowlingWraithsModel(model.getX(), model.getY(), model.getBounds().width, model.getBounds().height));
                AudioManager.playScream();
            } else {
                model.setCastingUp(false);
                model.setCastTimer(0.4f);
                float spellX = model.isFacingRight() ? model.getX() + 50f : model.getX() - 50f;
                float spellY = model.getY() + 20f;
                activeSpells.add(new VengefulSpiritModel(spellX, spellY, model.isFacingRight()));
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C) && model.isCanDash() && model.getDashCooldownTimer() <= 0) {
            model.setDashing(true);
            model.setCanDash(false);
            model.setDashTimer(0.3f);

            float dashSpeed = Constance.KNIGHT_SPEED * 2.5f;
            if (model.isCharmEquipped(KnightModel.Charm.SHARP_SHADOW)) {
                dashSpeed *= 1.2f;
            }

            model.setVelocityX(model.isFacingRight() ? dashSpeed : -dashSpeed);
            model.setVelocityY(0);

            model.setFocusing(false);
            model.setWallSliding(false);
            AudioManager.playDash();
            return;
        }

        model.setVelocityX(0);
        model.setLookingUp(false);
        model.setLookingDown(false);

        if (model.getWallJumpTimer() > 0) {
            model.setWallJumpTimer(model.getWallJumpTimer() - delta);
        }

        if (!model.isDashing() && !model.isFocusing()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !model.isAttacking()) {
                model.setAttacking(true);
                model.setAttackTimer(0f);

                AudioManager.playNailSlash();

                Rectangle attackBox = new Rectangle();
                float reach = 80f;

                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    model.setAttackDirection(1);
                    attackBox.set(model.getX(), model.getY() + model.getBounds().height, model.getBounds().width, reach);
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !model.isGrounded()) {
                    model.setAttackDirection(-1);
                    attackBox.set(model.getX(), model.getY() - reach, model.getBounds().width, reach);
                }
                else {
                    model.setAttackDirection(0);
                    model.setAltSlash(!model.isAltSlash());
                    float ax = model.isFacingRight() ? model.getX() + model.getBounds().width : model.getX() - reach;
                    attackBox.set(ax, model.getY(), reach, model.getBounds().height);
                }

                if (model.getAttackDirection() == -1) {
                    for (Rectangle spike : mapModel.getDeadlyBounds()) {
                        if (attackBox.overlaps(spike)) {
                            model.setVelocityY(Constance.JUMP_FORCE * 1.5f);
                            model.setCanDoubleJump(true);
                            break;
                        }
                    }
                }

                int baseDamage = model.isCharmEquipped(KnightModel.Charm.UNBREAKABLE_STRENGTH) ? 2 : 1;
                float soulGain = model.isCharmEquipped(KnightModel.Charm.SOUL_CATCHER) ? 17f : 11f;
                boolean isHeavy = model.isCharmEquipped(KnightModel.Charm.HEAVY_BLOW);

                for (EnemyModel enemy : enemies) {
                    if (!enemy.isDead() && attackBox.overlaps(enemy.getBounds())) {
                        boolean wasEnemyDead = enemy.isDead();
                        enemy.takeDamage(baseDamage, model.getX(), isHeavy);

                        if (!wasEnemyDead && enemy.isDead()) {
                            AchievementManager.getInstance().registerEnemyKill(enemy.getClass().getSimpleName());
                        }

                        model.setCurrentSoul(model.getCurrentSoul() + soulGain);
                        AudioManager.playSoulGain();

                        if (model.getAttackDirection() == -1) {
                            model.setVelocityY(Constance.JUMP_FORCE * 1.5f);
                            model.setCanDoubleJump(true);
                        }
                    }
                }

                if (boss != null && !boss.isDead() && !boss.isStunTriggered() && attackBox.overlaps(boss.getBounds())) {
                    boolean wasBossDead = boss.isDead();
                    boss.takeDamage(baseDamage);

                    if (!wasBossDead && boss.isDead()) {
                        AchievementManager.getInstance().unlockAchievement("Defeat False Knight");
                    }

                    model.setCurrentSoul(model.getCurrentSoul() + soulGain);
                    AudioManager.playSoulGain();

                    if (model.getAttackDirection() == -1) {
                        model.setVelocityY(Constance.JUMP_FORCE * 1.5f);
                        model.setCanDoubleJump(true);
                    }
                }

                if (boss != null && !boss.isDead() && boss.isStunTriggered() && attackBox.overlaps(boss.getBounds())) {
                    model.setCurrentSoul(model.getCurrentSoul() + soulGain);
                    AudioManager.playSoulGain();

                    if (model.getAttackDirection() == -1) {
                        model.setVelocityY(Constance.JUMP_FORCE * 1.5f);
                        model.setCanDoubleJump(true);
                    }
                }

                if (zote != null && attackBox.overlaps(zote.getBounds())) {
                    zote.takeDamage();
                }
            }
        }

        if (model.getWallJumpTimer() <= 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                model.setVelocityX(-Constance.KNIGHT_SPEED);
                model.setFacingRight(false);
                model.setLanding(false);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                model.setVelocityX(Constance.KNIGHT_SPEED);
                model.setFacingRight(true);
                model.setLanding(false);
            }
        }

        if (model.isGrounded() && model.getVelocityX() == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (!model.isLanding()) model.setLookingUp(true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                model.setLookingDown(true);
                model.setLanding(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (model.isGrounded()) {
                model.setVelocityY(Constance.JUMP_FORCE);
                model.setGrounded(false);
                model.setLanding(false);
            } else if (model.isWallSliding()) {
                model.setVelocityY(Constance.JUMP_FORCE * 0.9f);
                model.setVelocityX(-model.getWallDirection() * Constance.KNIGHT_SPEED * 1.5f);
                model.setWallSliding(false);
                model.setWallJumpTimer(0.25f);
                model.setFacingRight(model.getWallDirection() == 1);
            } else if (model.isCanDoubleJump()) {
                model.setVelocityY(Constance.JUMP_FORCE * 0.9f);
                model.setCanDoubleJump(false);
                model.setDoubleJumping(true);
                model.setWallSliding(false);
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (model.getVelocityY() > 0 && model.getWallJumpTimer() <= 0) {
                model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * 2.5f * delta));
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && model.isGrounded()) {
            model.setVelocityX(0);
            model.setFocusing(true);
            if (model.getCurrentHealth() < model.getMaxHealth() && model.getCurrentSoul() >= 20) {
                model.setFocusTimer(model.getFocusTimer() + delta);

                float healLimit = model.isCharmEquipped(KnightModel.Charm.QUICK_FOCUS) ? 0.6f : 1.0f;

                if (model.getFocusTimer() >= healLimit) {
                    model.setCurrentSoul(model.getCurrentSoul() - 20);
                    model.heal(1);
                    model.setFocusTimer(0f);

                    AudioManager.playFocus();
                }
            } else {
                model.setFocusTimer(0f);
            }
        } else {
            model.setFocusing(false);
            model.setFocusTimer(0f);
        }
    }

    private void applyPhysics(float delta, MapModel mapModel) {

        if (model.isCasting()) return;

        boolean touchingWall = false;

        model.setX(model.getX() + (model.getVelocityX() * delta));
        model.updateBounds();

        if (checkHazards(mapModel)) return;

        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (model.getBounds().overlaps(solid)) {

                Rectangle lowerHalf = new Rectangle(
                    model.getBounds().x,
                    model.getBounds().y,
                    model.getBounds().width,
                    model.getBounds().height * 0.5f
                );

                boolean touchesLowerHalf = lowerHalf.overlaps(solid);

                if (model.getVelocityX() > 0) {
                    model.setX(solid.x - model.getBounds().width);
                    if (touchesLowerHalf) {
                        touchingWall = true;
                        model.setWallDirection(1);
                    }
                }
                else if (model.getVelocityX() < 0) {
                    model.setX(solid.x + solid.width);
                    if (touchesLowerHalf) {
                        touchingWall = true;
                        model.setWallDirection(-1);
                    }
                }
                model.setVelocityX(0);
                if (model.isDashing()) { model.setDashing(false); }
                model.updateBounds();
            }
        }

        if (touchingWall && !model.isGrounded() && model.getVelocityY() < 0) {
            if ((model.getWallDirection() == 1 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
                (model.getWallDirection() == -1 && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                model.setWallSliding(true);
            } else {
                model.setWallSliding(false);
            }
        } else {
            model.setWallSliding(false);
        }

        if (!model.isDashing()) {
            if (model.isWallSliding()) {
                float maxSlideSpeed = -Constance.KNIGHT_SPEED * 0.4f;
                model.setVelocityY(Math.max(model.getVelocityY() - (Constance.GRAVITY * delta), maxSlideSpeed));
            } else {
                model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * delta));
            }
        }

        model.setY(model.getY() + (model.getVelocityY() * delta));
        model.updateBounds();

        if (checkHazards(mapModel)) return;

        model.setGrounded(false);

        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (model.getBounds().overlaps(solid)) {
                if (model.getVelocityY() < 0) {
                    model.setY(solid.y + solid.height);
                    model.setVelocityY(0);
                    model.setGrounded(true);
                }
                else if (model.getVelocityY() > 0) {
                    model.setY(solid.y - model.getBounds().height);
                    model.setVelocityY(0);
                }
                model.updateBounds();
            }
        }
    }

    private boolean checkHazards(MapModel mapModel) {
        for (Rectangle spike : mapModel.getDeadlyBounds()) {
            if (model.getBounds().overlaps(spike)) {

                if (!model.isInvulnerable()) AudioManager.playTakeDamage();
                model.takeDamage(1);

                if (!model.isDead()) {
                    model.setX(model.getLastSafeX());
                    model.setY(model.getLastSafeY() + 10);
                    model.setVelocityX(0);
                    model.setVelocityY(0);
                    model.updateBounds();
                }
                return true;
            }
        }
        return false;
    }
}
