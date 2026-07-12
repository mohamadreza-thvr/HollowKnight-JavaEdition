package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class EnemyAnimationManager {
    private List<Texture> loadedTextures;

    private Animation<TextureRegion> crawlidWalk, crawlidTurn, crawlidDeathAir, crawlidDeathLand;
    private Animation<TextureRegion> mosquitoIdle, mosquitoTurn, mosquitoAnticipate, mosquitoAttack, mosquitoDeathAir, mosquitoDeathLand;
    private Animation<TextureRegion> huskIdle, huskWalk, huskTurn, huskAnticipate, huskLunge, huskDeath;
    private Animation<TextureRegion> crystalWalk, crystalTurn, crystalDeathAir, crystalDeathLand;
    private Animation<TextureRegion> hunterFly, hunterTurn, hunterAttackRecover, hunterDeathAir, hunterDeathLand;

    private Animation<TextureRegion> guardianIdle, guardianRun, guardianShoot, guardianTurn, guardianEvade, guardianDeathAir, guardianDeathLand;
    private Animation<TextureRegion> guardianLaser;

    private float globalStateTime = 0f;

    public EnemyAnimationManager() {
        loadedTextures = new ArrayList<>();
        loadAnimations();
    }

    private void loadAnimations() {
        crawlidWalk = createSpriteSheet("animation/Crawlid/Walk.png", 4, 0.3f);
        crawlidTurn = createSpriteSheet("animation/Crawlid/Turn.png", 2, 0.3f);
        crawlidDeathAir = createSpriteSheet("animation/Crawlid/Death Air.png", 3, 0.3f);
        crawlidDeathLand = createSpriteSheet("animation/Crawlid/Death Land.png", 2, 0.3f);

        mosquitoIdle = createSpriteSheet("animation/Mosquito/Idle.png", 8, 0.6f);
        mosquitoTurn = createSpriteSheet("animation/Mosquito/Turn.png", 2, 0.3f);
        mosquitoAnticipate = createSpriteSheet("animation/Mosquito/Attack Anticipate.png", 6, 0.3f);
        mosquitoAttack = createSpriteSheet("animation/Mosquito/Attack.png", 3, 0.3f);
        mosquitoDeathAir = createSpriteSheet("animation/Mosquito/Death Air.png", 3, 0.3f);
        mosquitoDeathLand = createSpriteSheet("animation/Mosquito/Death Land.png", 2, 0.3f);

        huskIdle = createSpriteSheet("animation/Husk/Idle.png", 6, 0.5f);
        huskWalk = createSpriteSheet("animation/Husk/Walk.png", 7, 0.5f);
        huskTurn = createSpriteSheet("animation/Husk/Turn.png", 2, 0.2f);
        huskAnticipate = createSpriteSheet("animation/Husk/Attack Anticipate.png", 5, 0.25f);
        huskLunge = createSpriteSheet("animation/Husk/Attack Lunge.png", 12, 0.25f);
        huskDeath = createSpriteSheet("animation/Husk/Death Land.png", 8, 0.25f);

        crystalWalk = createSpriteSheet("animation/Crystal_Crawler/Walk.png", 4, 0.35f);
        crystalTurn = createSpriteSheet("animation/Crystal_Crawler/Turn.png", 2, 0.35f);
        crystalDeathAir = createSpriteSheet("animation/Crystal_Crawler/Death Air.png", 3, 0.35f);
        crystalDeathLand = createSpriteSheet("animation/Crystal_Crawler/Death Land.png", 2, 0.35f);

        hunterFly = createSpriteSheet("animation/Crystal_Hunter/Fly.png", 4, 0.3f);
        hunterTurn = createSpriteSheet("animation/Crystal_Hunter/Turn To Fly.png", 3, 0.3f);
        hunterAttackRecover = createSpriteSheet("animation/Crystal_Hunter/Attack.png", 4, 0.3f);
        hunterDeathAir = createSpriteSheet("animation/Crystal_Hunter/Death Air.png", 3, 0.3f);
        hunterDeathLand = createSpriteSheet("animation/Crystal_Hunter/Death Land.png", 1, 0.3f);

        guardianIdle = createSpriteSheet("animation/Crystallized/Idle.png", 5, 0.3f);
        guardianRun = createSpriteSheet("animation/Crystallized/Run.png", 6, 0.2f);
        guardianShoot = createSpriteSheet("animation/Crystallized/Shoot.png", 7, 0.1f);
        guardianTurn = createSpriteSheet("animation/Crystallized/Turn.png", 3, 0.2f);
        guardianEvade = createSpriteSheet("animation/Crystallized/Evade.png", 7, 0.2f);
        guardianDeathAir = createSpriteSheet("animation/Crystallized/Death Air.png", 3, 0.2f);
        guardianDeathLand = createSpriteSheet("animation/Crystallized/Death Land.png", 3, 0.2f);

        guardianLaser = createSpriteSheet("animation/Effects/CrystalLaser.png", 15, 0.05f);
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

    public TextureRegion getGuardianLaserFrame(float stateTime) {
        return guardianLaser.getKeyFrame(stateTime, false);
    }

    public TextureRegion getCurrentFrame(float delta, EnemyModel enemy) {
        globalStateTime += delta;
        float animTime = globalStateTime;
        Animation<TextureRegion> currentAnim = null;
        boolean loop = true;

        if (enemy instanceof CrawlidModel) {
            CrawlidModel crawlid = (CrawlidModel) enemy;
            if (crawlid.isDead()) {
                currentAnim = crawlid.isHitGroundAfterDeath() ? crawlidDeathLand : crawlidDeathAir; loop = false;
            } else if (crawlid.getCurrentState() == EnemyState.TURN) {
                currentAnim = crawlidTurn; loop = false; animTime = crawlid.getStateTimer();
            } else { currentAnim = crawlidWalk; }
        }
        else if (enemy instanceof CrystalCrawlerModel) {
            CrystalCrawlerModel crawler = (CrystalCrawlerModel) enemy;
            if (crawler.isDead()) {
                currentAnim = crawler.isHitGroundAfterDeath() ? crystalDeathLand : crystalDeathAir; loop = false;
            } else if (crawler.getCurrentState() == EnemyState.TURN) {
                currentAnim = crystalTurn; loop = false; animTime = crawler.getStateTimer();
            } else { currentAnim = crystalWalk; }
        }
        else if (enemy instanceof CrystalHunterModel) {
            CrystalHunterModel hunter = (CrystalHunterModel) enemy;
            if (hunter.isDead()) {
                currentAnim = hunter.isHitGroundAfterDeath() ? hunterDeathLand : hunterDeathAir; loop = false; animTime = hunter.getStateTimer();
            } else if (hunter.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = hunterFly;
            } else if (hunter.getCurrentState() == EnemyState.DASH) {
                currentAnim = hunterAttackRecover; loop = false; animTime = hunter.getStateTimer();
            } else if (hunter.getCurrentState() == EnemyState.TURN) {
                currentAnim = hunterTurn; loop = false; animTime = hunter.getStateTimer();
            } else { currentAnim = hunterFly; }
        }
        else if (enemy instanceof CrystalGuardianModel) {
            CrystalGuardianModel guardian = (CrystalGuardianModel) enemy;
            if (guardian.isDead()) {
                currentAnim = guardian.isHitGroundAfterDeath() ? guardianDeathLand : guardianDeathAir; loop = false; animTime = guardian.getStateTimer();
            } else if (guardian.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = guardianShoot; loop = false; animTime = guardian.getStateTimer();
            } else if (guardian.getCurrentState() == EnemyState.DASH) {
                currentAnim = guardianRun; animTime = guardian.getStateTimer();
            } else if (guardian.getCurrentState() == EnemyState.TURN) {
                currentAnim = guardianTurn; loop = false; animTime = guardian.getStateTimer();
            } else { currentAnim = guardianIdle; }
        }
        else if (enemy instanceof MosquitoModel) {
            MosquitoModel mosquito = (MosquitoModel) enemy;
            if (mosquito.isDead()) {
                currentAnim = mosquito.isHitGroundAfterDeath() ? mosquitoDeathLand : mosquitoDeathAir; loop = false; animTime = mosquito.getStateTimer();
            } else if (mosquito.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = mosquitoAnticipate; loop = false; animTime = mosquito.getStateTimer();
            } else if (mosquito.getCurrentState() == EnemyState.DASH) {
                currentAnim = mosquitoAttack;
            } else if (mosquito.getCurrentState() == EnemyState.TURN) {
                currentAnim = mosquitoTurn; loop = false; animTime = mosquito.getStateTimer();
            } else { currentAnim = mosquitoIdle; }
        } else if (enemy instanceof HuskModel) {
            HuskModel husk = (HuskModel) enemy;
            if (husk.isDead()) {
                currentAnim = huskDeath; loop = false; animTime = husk.getStateTimer();
            } else if (husk.getCurrentState() == EnemyState.ANTICIPATE) {
                currentAnim = huskAnticipate; loop = false; animTime = husk.getStateTimer();
            } else if (husk.getCurrentState() == EnemyState.DASH) {
                currentAnim = huskLunge;
            } else if (husk.getCurrentState() == EnemyState.TURN) {
                currentAnim = huskTurn; loop = false; animTime = husk.getStateTimer();
            } else if (husk.getCurrentState() == EnemyState.WALK) {
                currentAnim = huskWalk;
            } else { currentAnim = huskIdle; }
        }

        TextureRegion frame = currentAnim.getKeyFrame(animTime, loop);
        if (frame.isFlipX() != enemy.isFacingRight()) {
            frame.flip(true, false);
        }
        return frame;
    }

    public void dispose() {
        for (Texture tex : loadedTextures) tex.dispose();
    }
}
