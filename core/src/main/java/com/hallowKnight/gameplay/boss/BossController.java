package com.hallowKnight.gameplay.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;

public class BossController {

    private Sound jumpSound;
    private Sound landSound;
    private Sound[] attackSounds;

    public BossController() {
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/boss_jump.wav"));
        landSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/boss_land.wav"));

        attackSounds = new Sound[3];
        attackSounds[0] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/boss_attack_1.wav"));
        attackSounds[1] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/boss_attack_2.wav"));
        attackSounds[2] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/boss_attack_3.wav"));
    }

    public void update(BossModel boss, float delta, KnightModel knight, MapModel map) {
        if (delta > 0.05f) delta = 0.05f;

        boss.setSpellHit(false);
        boss.setWraithHit(false);

        if (boss.getCameraShakeTimer() > 0) {
            boss.setCameraShakeTimer(boss.getCameraShakeTimer() - delta);
        }

        if (boss.getInvulnerabilityTimer() > 0) {
            boss.setInvulnerabilityTimer(boss.getInvulnerabilityTimer() - delta);
        }

        if (boss.getHitTimer() > 0) {
            boss.setHitTimer(boss.getHitTimer() - delta);
            if (boss.getHitTimer() <= 0) {
                boss.setRecentHitCount(0);
            }
        }

        if (boss.getHitReactTimer() > 0) {
            boss.setHitReactTimer(boss.getHitReactTimer() - delta);
            boss.setHitReactStateTime(boss.getHitReactStateTime() + delta);
        }

        if (boss.isDead()) {
            updateDeathSequence(boss, delta, map);
            applyPhysics(boss, delta, map);
            return;
        }

        if (!boss.isStunTriggered() && boss.getHp() <= BossModel.STUN_HP_TRIGGER) {
            boss.setStunTriggered(true);
            boss.setPhase2(true);
            enterStun(boss);
            applyPhysics(boss, delta, map);
            return;
        }

        boss.setStateTimer(boss.getStateTimer() + delta);

        for (int i = boss.getShockwaves().size() - 1; i >= 0; i--) {
            ShockwaveModel sw = boss.getShockwaves().get(i);
            sw.update(delta);

            if (sw.getBounds().overlaps(knight.getBounds()) && !knight.isInvulnerable()) {
                knight.takeDamage(2);
            }

            boolean hitWall = false;
            for (Rectangle solid : map.getSolidBounds()) {
                if (sw.getBounds().overlaps(solid)) {
                    hitWall = true;
                    break;
                }
            }
            if (hitWall) boss.getShockwaves().remove(i);
        }

        switch (boss.getCurrentState()) {
            case IDLE:
                handleIdle(boss, delta, knight);
                break;
            case RUN_ANTIC:
                if (boss.getStateTimer() >= BossModel.CHARGE_ANTICIPATION) {
                    enterChargeRun(boss, knight);
                }
                break;
            case MACE_SLAM:
                handleMaceSlam(boss, delta, knight);
                break;
            case CHARGE_RUN:
                handleChargeRun(boss, delta, map, knight);
                break;
            case OFFENSIVE_LEAP:
            case DEFENSIVE_LEAP:
            case LEAP_SLAM:
            case JUMP:
                handleJump(boss, delta, map, knight);
                break;
            case JUMP_ANTIC:
                if (boss.getStateTimer() >= BossModel.JUMP_ANTIC_DURATION) {
                    releaseJump(boss, knight);
                }
                break;
            case LAND:
                if (boss.getStateTimer() >= BossModel.LAND_DURATION) {
                    boss.setCurrentState(BossState.IDLE);
                    boss.setStateTimer(0f);
                    boss.setActionCooldown(boss.isPhase2() ? 0.3f : 0.8f);
                }
                break;
            case TURN:
                if (boss.getStateTimer() >= BossModel.TURN_DURATION) {
                    boss.setFacingRight(knight.getX() > boss.getX());
                    boss.setCurrentState(BossState.IDLE);
                    boss.setStateTimer(0f);
                    boss.setActionCooldown(boss.isPhase2() ? 0.2f : 0.5f);
                }
                break;
            case STUN:
                if (boss.getStateTimer() >= BossModel.STUN_DURATION) {
                    enterStunRecover(boss);
                }
                break;
            case STUN_RECOVER:
                if (boss.getStateTimer() >= BossModel.STUN_RECOVER_DURATION) {
                    boss.setCurrentState(BossState.IDLE);
                    boss.setStateTimer(0f);
                }
                break;
            default:
                break;
        }

        applyPhysics(boss, delta, map);
    }

    private void handleIdle(BossModel boss, float delta, KnightModel knight) {
        boss.setVelocityX(0);
        float currentCooldown = boss.getActionCooldown();
        if (currentCooldown > 0) {
            boss.setActionCooldown(currentCooldown - delta);
        }

        if (boss.getStateTimer() >= BossModel.IDLE_DURATION && boss.getActionCooldown() <= 0) {
            decideNextMove(boss, knight);
        }
    }

    private void decideNextMove(BossModel boss, KnightModel knight) {
        float dist = Vector2.dst(boss.getX(), boss.getY(), knight.getX(), knight.getY());

        boolean knightIsRight = knight.getX() > boss.getX();
        if (knightIsRight != boss.isFacingRight() && boss.getCurrentState() != BossState.TURN) {
            boss.setCurrentState(BossState.TURN);
            boss.setStateTimer(0f);
            return;
        }

        if (boss.getRecentHitCount() >= 2) {
            boss.setRecentHitCount(0);
            boss.recordMove(BossState.DEFENSIVE_LEAP);
            executeMove(boss, BossState.DEFENSIVE_LEAP, knight);
            return;
        }

        BossState chosen = pickMove(boss, dist, knight);

        if (boss.isSpam(chosen)) {
            chosen = fallbackMove(chosen, dist);
        }

        boss.recordMove(chosen);
        executeMove(boss, chosen, knight);
    }

    private BossState pickMove(BossModel boss, float dist, KnightModel knight) {
        float rand = MathUtils.random(1f);

        if (dist < BossModel.CLOSE_RANGE) {
            return BossState.MACE_SLAM;

        } else if (dist < BossModel.MID_RANGE) {
            if (boss.isPhase2() && rand < 0.50f) return BossState.LEAP_SLAM;
            else if (rand < 0.70f) return BossState.OFFENSIVE_LEAP;
            else return BossState.CHARGE_RUN;

        } else {
            if (boss.isPhase2() && rand < 0.50f) return BossState.LEAP_SLAM;
            else if (rand < 0.80f) return BossState.CHARGE_RUN;
            else return BossState.OFFENSIVE_LEAP;
        }
    }

    private BossState fallbackMove(BossState spammed, float dist) {
        if (spammed == BossState.MACE_SLAM)      return BossState.OFFENSIVE_LEAP;
        if (spammed == BossState.CHARGE_RUN)     return BossState.MACE_SLAM;
        if (spammed == BossState.OFFENSIVE_LEAP) return BossState.CHARGE_RUN;
        if (spammed == BossState.LEAP_SLAM)      return BossState.CHARGE_RUN;
        return BossState.MACE_SLAM;
    }

    private void executeMove(BossModel boss, BossState move, KnightModel knight) {
        boss.setFacingRight(knight.getX() > boss.getX());
        boss.setStateTimer(0f);

        switch (move) {
            case MACE_SLAM:
                boss.setCurrentState(BossState.MACE_SLAM);
                boss.setVelocityX(0);
                boss.setAttackDamageDealt(false);
                playRandomAttackSound();
                break;
            case CHARGE_RUN:
                boss.setCurrentState(BossState.RUN_ANTIC);
                boss.setVelocityX(0);
                playRandomAttackSound();
                break;
            case OFFENSIVE_LEAP:
            case LEAP_SLAM:
                boss.setCurrentState(BossState.JUMP_ANTIC);
                boss.setVelocityX(0);
                break;
            case DEFENSIVE_LEAP:
                boss.setCurrentState(BossState.DEFENSIVE_LEAP);
                float dir = boss.isFacingRight() ? -1f : 1f;
                boss.setVelocityX(dir * BossModel.JUMP_VEL_X * 0.8f);
                boss.setVelocityY(BossModel.JUMP_VEL_Y * 0.7f);
                break;
            default:
                boss.setCurrentState(BossState.IDLE);
                break;
        }
    }

    private void playRandomAttackSound() {
        if (attackSounds != null && attackSounds.length > 0) {
            int index = MathUtils.random(attackSounds.length - 1);
            if (attackSounds[index] != null) {
                attackSounds[index].play(1.0f);
            }
        }
    }

    private void handleMaceSlam(BossModel boss, float delta, KnightModel knight) {
        float t = boss.getStateTimer();
        float windup  = BossModel.MACE_SLAM_WINDUP;
        float active  = windup + BossModel.MACE_SLAM_ACTIVE;
        float recover = active + BossModel.MACE_SLAM_RECOVER;

        if (t >= windup && t < active && !boss.isAttackDamageDealt()) {
            float hbW = 120f, hbH = 80f;

            float hbX = boss.isFacingRight()
                ? boss.getX() + boss.getBounds().width - 10f
                : boss.getX() - hbW + 10f;
            float hbY = boss.getY();

            boss.getAttackHitbox().set(hbX, hbY, hbW, hbH);
            boss.setAttackDamageDealt(true);

            if (boss.isPhase2() && boss.getLastMove() == BossState.LEAP_SLAM) {
                boss.getShockwaves().add(new ShockwaveModel(hbX, boss.getY() + 10f, boss.isFacingRight()));
                boss.triggerCameraShake(12f, 0.5f);
            } else {
                boss.triggerCameraShake(5f, 0.2f);
            }
        }

        if (t >= active) {
            boss.getAttackHitbox().set(0, 0, 0, 0);
        }

        if (t >= recover) {
            boss.setCurrentState(BossState.IDLE);
            boss.setStateTimer(0f);
            boss.setActionCooldown(boss.isPhase2() ? 0.3f : 0.8f);
        }
    }

    private void enterChargeRun(BossModel boss, KnightModel knight) {
        float speed = boss.isPhase2()
            ? BossModel.CHARGE_SPEED * BossModel.PHASE2_SPEED_MULT
            : BossModel.CHARGE_SPEED;
        boss.setVelocityX(boss.isFacingRight() ? speed : -speed);
        boss.setCurrentState(BossState.CHARGE_RUN);
        boss.setStateTimer(0f);
    }

    private void handleChargeRun(BossModel boss, float delta, MapModel map, KnightModel knight) {
        boolean passedKnight = boss.isFacingRight()
            ? boss.getX() > knight.getX() + 80f
            : boss.getX() < knight.getX() - 80f;

        if (passedKnight || boss.getStateTimer() > 2.0f) {
            boss.setVelocityX(0);
            boss.triggerCameraShake(4f, 0.15f);
            boss.setCurrentState(BossState.IDLE);
            boss.setStateTimer(0f);
            boss.setActionCooldown(boss.isPhase2() ? 0.4f : 1.0f);
        }
    }

    private void releaseJump(BossModel boss, KnightModel knight) {
        boolean isOffensive = boss.getLastMove() == BossState.OFFENSIVE_LEAP;
        boolean isLeapSlam = boss.getLastMove() == BossState.LEAP_SLAM;

        float velY = BossModel.JUMP_VEL_Y;
        float velX;

        if (isOffensive) {
            float distanceToKnight = knight.getX() - boss.getX();
            velX = distanceToKnight * 1.3f;
            velX = MathUtils.clamp(velX, -700f, 700f);
        }
        else if (isLeapSlam) {
            float distanceToKnight = knight.getX() - boss.getX();
            velX = distanceToKnight * 0.85f;
            velX = MathUtils.clamp(velX, -500f, 500f);
            velY = BossModel.JUMP_VEL_Y * 1.25f;
        }
        else {
            velX = boss.isFacingRight() ? BossModel.JUMP_VEL_X : -BossModel.JUMP_VEL_X;
            velX *= 0.6f;
            velY *= 1.1f;
        }

        if (boss.isPhase2() && !isLeapSlam) {
            velX *= BossModel.PHASE2_SPEED_MULT;
        }

        boss.setVelocityX(velX);
        boss.setVelocityY(velY);
        boss.setCurrentState(BossState.JUMP);
        boss.setStateTimer(0f);
        boss.setGrounded(false);

        if (jumpSound != null) {
            jumpSound.play(1.0f);
        }
    }

    private void handleJump(BossModel boss, float delta, MapModel map, KnightModel knight) {
        if (boss.isGrounded() && boss.getStateTimer() > 0.15f) {
            boss.setVelocityX(0);

            if (boss.getLastMove() == BossState.LEAP_SLAM) {
                boss.setFacingRight(knight.getX() > boss.getX());

                boss.setCurrentState(BossState.MACE_SLAM);
                boss.setStateTimer(0f);
                boss.setAttackDamageDealt(false);
                playRandomAttackSound();

                if (landSound != null) landSound.play(1.0f);
            } else {
                boss.setCurrentState(BossState.LAND);
                boss.setStateTimer(0f);
                boss.triggerCameraShake(6f, 0.25f);

                if (landSound != null) landSound.play(1.0f);
            }
        }
    }

    private void enterStun(BossModel boss) {
        boss.setCurrentState(BossState.STUN);
        boss.setStateTimer(0f);
        boss.setVelocityX(0);
        boss.setVelocityY(0);
        boss.triggerCameraShake(10f, 0.5f);
    }

    private void enterStunRecover(BossModel boss) {
        boss.setCurrentState(BossState.STUN_RECOVER);
        boss.setStateTimer(0f);
    }

    private void updateDeathSequence(BossModel boss, float delta, MapModel map) {
        boss.setStateTimer(boss.getStateTimer() + delta);

        if (!boss.isDeathStarted()) {
            boss.setDeathStarted(true);
            boss.setCurrentState(BossState.DEATH_FALL);
            boss.setStateTimer(0f);
            boss.setVelocityY(400f);
            boss.setVelocityX(boss.isFacingRight() ? -100f : 100f);
            boss.triggerCameraShake(12f, 0.6f);
        }

        switch (boss.getCurrentState()) {
            case DEATH_FALL:
                if (boss.isGrounded() && boss.getStateTimer() > 0.1f) {
                    boss.setCurrentState(BossState.DEATH_LAND);
                    boss.setStateTimer(0f);
                    boss.setVelocityX(0);
                    boss.setVelocityY(0);
                    boss.triggerCameraShake(15f, 0.8f);

                    if (landSound != null) landSound.play(1.0f);
                }
                break;
            case DEATH_LAND:
                if (boss.getStateTimer() >= 1.1f) {
                    boss.setCurrentState(BossState.STUN);
                    boss.setStateTimer(0f);
                }
                break;
            case STUN:
                boss.setVelocityX(0);
                boss.setVelocityY(0);
                break;
            default:
                break;
        }
    }

    private void applyPhysics(BossModel boss, float delta, MapModel map) {
        boss.setGrounded(false);

        boss.setX(boss.getX() + boss.getVelocityX() * delta);
        boss.updateBounds();

        for (Rectangle solid : map.getSolidBounds()) {
            if (boss.getBounds().overlaps(solid) && solid.y + solid.height > boss.getY() + 5f) {
                if (boss.getVelocityX() > 0) {
                    boss.setX(solid.x - boss.getBounds().width);
                    if (boss.getCurrentState() == BossState.CHARGE_RUN) {
                        boss.setVelocityX(0);
                        boss.setCurrentState(BossState.IDLE);
                        boss.setStateTimer(0f);
                        boss.triggerCameraShake(5f, 0.2f);
                        boss.setActionCooldown(boss.isPhase2() ? 0.3f : 0.8f);
                    }
                } else if (boss.getVelocityX() < 0) {
                    boss.setX(solid.x + solid.width);
                    if (boss.getCurrentState() == BossState.CHARGE_RUN) {
                        boss.setVelocityX(0);
                        boss.setCurrentState(BossState.IDLE);
                        boss.setStateTimer(0f);
                        boss.triggerCameraShake(5f, 0.2f);
                        boss.setActionCooldown(boss.isPhase2() ? 0.3f : 0.8f);
                    }
                }
                boss.setVelocityX(0);
                boss.updateBounds();
            }
        }

        boss.setVelocityY(boss.getVelocityY() - Constance.GRAVITY * delta);
        boss.setY(boss.getY() + boss.getVelocityY() * delta);
        boss.updateBounds();

        for (Rectangle solid : map.getSolidBounds()) {
            if (boss.getBounds().overlaps(solid)) {
                if (boss.getVelocityY() < 0) {
                    boss.setY(solid.y + solid.height);
                    boss.setVelocityY(0);
                    boss.setGrounded(true);
                } else if (boss.getVelocityY() > 0) {
                    boss.setY(solid.y - boss.getBounds().height);
                    boss.setVelocityY(0);
                }
                boss.updateBounds();
            }
        }
    }

    public void dispose() {
        if (jumpSound != null) jumpSound.dispose();
        if (landSound != null) landSound.dispose();
        if (attackSounds != null) {
            for (Sound s : attackSounds) {
                if (s != null) s.dispose();
            }
        }
    }
}
