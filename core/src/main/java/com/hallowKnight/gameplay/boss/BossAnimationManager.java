package com.hallowKnight.gameplay.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class BossAnimationManager {

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> runAntic;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> attackAntic;
    private Animation<TextureRegion> attack;
    private Animation<TextureRegion> attackRecover;
    private Animation<TextureRegion> jumpAntic;
    private Animation<TextureRegion> jump;
    private Animation<TextureRegion> land;
    private Animation<TextureRegion> turn;
    private Animation<TextureRegion> stun;
    private Animation<TextureRegion> stunRecover;
    private Animation<TextureRegion> deathHit;
    private Animation<TextureRegion> deathFall;
    private Animation<TextureRegion> deathLand;
    private Animation<TextureRegion> shockwave;

    private final List<Texture> loadedTextures = new ArrayList<>();

    public BossAnimationManager() {
        idle         = load("animation/False_knight/Idle.png",          5, 1,  0.12f);
        runAntic     = load("animation/False_knight/Run Antic.png",     2, 1,  0.10f);
        run          = load("animation/False_knight/Run.png",           5, 1,  0.08f);
        attackAntic  = load("animation/False_knight/Attack Antic.png",  6, 1,  0.07f);
        attack       = load("animation/False_knight/Attack.png",        3, 1,  0.06f);
        attackRecover= load("animation/False_knight/Attack Recover.png",5, 1,  0.08f);
        jumpAntic    = load("animation/False_knight/Jump Antic.png",    3, 1,  0.10f);
        jump         = load("animation/False_knight/Jump.png",          4, 1,  0.12f);
        land         = load("animation/False_knight/Land.png",          5, 1,  0.08f);
        turn         = load("animation/False_knight/Turn.png",          2, 1,  0.10f);
        stun         = load("animation/False_knight/Body.png",          5, 1,  0.15f);
        stunRecover  = load("animation/False_knight/Stun Recover.png",  6, 1,  0.09f);
        deathHit     = load("animation/False_knight/DeathHit.png",      3, 1,  0.10f);
        deathFall    = load("animation/False_knight/DeathFall.png",     3, 1,  0.12f);
        deathLand    = load("animation/False_knight/DeathLand.png",    11, 1,  0.10f);
        shockwave = load("animation/False_knight/Shockwave.png", 8, 1, 0.08f);

        attackAntic .setPlayMode(Animation.PlayMode.NORMAL);
        attack      .setPlayMode(Animation.PlayMode.NORMAL);
        attackRecover.setPlayMode(Animation.PlayMode.NORMAL);
        runAntic    .setPlayMode(Animation.PlayMode.NORMAL);
        jumpAntic   .setPlayMode(Animation.PlayMode.NORMAL);
        land        .setPlayMode(Animation.PlayMode.NORMAL);
        turn        .setPlayMode(Animation.PlayMode.NORMAL);
        stunRecover .setPlayMode(Animation.PlayMode.NORMAL);
        deathHit    .setPlayMode(Animation.PlayMode.NORMAL);
        deathFall   .setPlayMode(Animation.PlayMode.NORMAL);
        deathLand   .setPlayMode(Animation.PlayMode.NORMAL);
    }

    public TextureRegion getCurrentFrame(BossModel boss, float delta) {
        if (boss.getHitReactTimer() > 0 &&
            (boss.getCurrentState() == BossState.STUN ||
                boss.getCurrentState() == BossState.STUN_RECOVER ||
                boss.getCurrentState() == BossState.DEATH_LAND ||
                boss.isDead())) {

            return deathHit.getKeyFrame(boss.getHitReactStateTime(), false);
        }

        float t = boss.getStateTimer();
        boolean loop = false;

        Animation<TextureRegion> anim;

        switch (boss.getCurrentState()) {
            case IDLE:
                anim = idle; loop = true; break;
            case RUN_ANTIC:
                anim = runAntic; break;
            case CHARGE_RUN:
                anim = run; loop = true; break;
            case MACE_SLAM:
                anim = getMaceSlamAnim(boss, t); break;
            case JUMP_ANTIC:
            case OFFENSIVE_LEAP:
            case DEFENSIVE_LEAP:
                anim = jumpAntic; break;
            case JUMP:
                anim = jump; loop = true; break;
            case LAND:
                anim = land; break;
            case TURN:
                anim = turn; break;
            case STUN:
                anim = stun; loop = true; break;
            case STUN_RECOVER:
                anim = stunRecover; break;
            case DEATH_HIT:
                anim = deathHit; loop = true; break;
            case DEATH_FALL:
                anim = deathFall; break;
            case DEATH_LAND:
                anim = deathLand; break;
            default:
                anim = idle; loop = true; break;
        }

        return anim.getKeyFrame(t, loop);
    }

    private Animation<TextureRegion> getMaceSlamAnim(BossModel boss, float t) {
        float windup = BossModel.MACE_SLAM_WINDUP;
        float active = windup + BossModel.MACE_SLAM_ACTIVE;
        if (t < windup)  return attackAntic;
        if (t < active)  return attack;
        return attackRecover;
    }

    public TextureRegion getShockwaveFrame(float stateTime, boolean facingRight) {
        TextureRegion frame = shockwave.getKeyFrame(stateTime, true);
        if (facingRight && frame.isFlipX()) {
            frame.flip(true, false);
        } else if (!facingRight && !frame.isFlipX()) {
            frame.flip(true, false);
        }
        return frame;
    }

    private Animation<TextureRegion> load(String fileName, int columns, int rows, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(fileName));
        loadedTextures.add(sheet);
        int frameWidth = sheet.getWidth() / columns;
        int frameHeight = sheet.getHeight() / rows;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }

    public void dispose() {
        for (Texture tex : loadedTextures) tex.dispose();
    }
}
