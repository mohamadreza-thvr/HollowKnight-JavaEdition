package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class EnemyAnimationManager {
    private List<Texture> loadedTextures;

    private Animation<TextureRegion> crawlidWalk;
    private Animation<TextureRegion> crawlidTurn;
    private Animation<TextureRegion> crawlidDeathAir;
    private Animation<TextureRegion> crawlidDeathLand;

    private Animation<TextureRegion> mosquitoIdle;
    private Animation<TextureRegion> mosquitoTurn;
    private Animation<TextureRegion> mosquitoAnticipate;
    private Animation<TextureRegion> mosquitoAttack;
    private Animation<TextureRegion> mosquitoDeathAir;
    private Animation<TextureRegion> mosquitoDeathLand;
    private Animation<TextureRegion> huskIdle, huskWalk, huskTurn, huskAnticipate, huskLunge, huskDeath;

    private float stateTime = 0f;

    public EnemyAnimationManager() {
        loadedTextures = new ArrayList<>();
        loadAnimations();
    }

    private void loadAnimations() {
        crawlidWalk = createSpriteSheet("animation/Crawlid/Walk.png", 4, 0.1f);
        crawlidTurn = createSpriteSheet("animation/Crawlid/Turn.png", 2, 0.1f);
        crawlidDeathAir = createSpriteSheet("animation/Crawlid/Death Air.png", 3, 0.1f);
        crawlidDeathLand = createSpriteSheet("animation/Crawlid/Death Land.png", 2, 0.1f);

        mosquitoIdle = createSpriteSheet("animation/Mosquito/Idle.png", 8, 0.1f);
        mosquitoTurn = createSpriteSheet("animation/Mosquito/Turn.png", 2, 0.1f);
        mosquitoAnticipate = createSpriteSheet("animation/Mosquito/Attack Anticipate.png", 6, 0.1f);
        mosquitoAttack = createSpriteSheet("animation/Mosquito/Attack.png", 3, 0.1f);
        mosquitoDeathAir = createSpriteSheet("animation/Mosquito/Death Air.png", 3, 0.1f);
        mosquitoDeathLand = createSpriteSheet("animation/Mosquito/Death Land.png", 2, 0.1f);

        huskIdle = createSpriteSheet("animation/Husk/Idle.png", 6, 0.3f);
        huskWalk = createSpriteSheet("animation/Husk/Walk.png", 7, 0.3f);
        huskTurn = createSpriteSheet("animation/Husk/Turn.png", 2, 0.1f);
        huskAnticipate = createSpriteSheet("animation/Husk/Attack Anticipate.png", 5, 0.1f);
        huskLunge = createSpriteSheet("animation/Husk/Attack Lunge.png", 12, 0.1f);
        huskDeath = createSpriteSheet("animation/Husk/Death Land.png", 8, 0.1f);
    }

    private Animation<TextureRegion> createSpriteSheet(String fileName, int columns, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(fileName));
        loadedTextures.add(sheet);
        int frameWidth = sheet.getWidth() / columns;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, sheet.getHeight());
        TextureRegion[] frames = new TextureRegion[columns];
        for (int i = 0; i < columns; i++) { frames[i] = tmp[0][i]; }
        return new Animation<TextureRegion>(frameDuration, frames);
    }

    public TextureRegion getCurrentFrame(float delta, EnemyModel enemy) {
        stateTime += delta;
        Animation<TextureRegion> currentAnim = null;
        boolean loop = true;

        if (enemy instanceof CrawlidModel) {
            CrawlidModel crawlid = (CrawlidModel) enemy;
            if (crawlid.isDead()) {
                currentAnim = crawlid.isHitGroundAfterDeath() ? crawlidDeathLand : crawlidDeathAir;
                loop = false;
            } else if (crawlid.getCurrentState() == EnemyState.TURN) {
                currentAnim = crawlidTurn;
                loop = false;
            } else {
                currentAnim = crawlidWalk;
            }
        }
        else if (enemy instanceof MosquitoModel) {
            MosquitoModel mosquito = (MosquitoModel) enemy;
            if (mosquito.isDead()) {
                currentAnim = mosquito.isHitGroundAfterDeath() ? mosquitoDeathLand : mosquitoDeathAir;
                loop = false;
            } else if (mosquito.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = mosquitoAnticipate;
                loop = false;
            } else if (mosquito.getCurrentState() == EnemyState.DASH) {
                currentAnim = mosquitoAttack;
            } else if (mosquito.getCurrentState() == EnemyState.TURN) {
                currentAnim = mosquitoTurn;
                loop = false;
            } else {
                currentAnim = mosquitoIdle;
            }
        } else if (enemy instanceof HuskModel) {
            HuskModel husk = (HuskModel) enemy;
            if (husk.isDead()) {
                currentAnim = huskDeath; loop = false;
            } else if (husk.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = huskAnticipate; loop = false;
            } else if (husk.getCurrentState() == EnemyState.DASH) {
                currentAnim = huskLunge;
            } else if (husk.getCurrentState() == EnemyState.TURN) {
                currentAnim = huskTurn; loop = false;
            } else if (husk.getCurrentState() == EnemyState.WALK) {
                currentAnim = huskWalk;
            } else {
                currentAnim = huskIdle;
            }
        }

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, loop);

        if (frame.isFlipX() != enemy.isFacingRight()) {
            frame.flip(true, false);
        }
        return frame;
    }

    public void dispose() {
        for (Texture tex : loadedTextures) tex.dispose();
    }
}
