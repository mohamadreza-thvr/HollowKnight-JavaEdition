package com.hallowKnight.gameplay.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hallowKnight.gameplay.knight.KnightModel;

import java.util.ArrayList;
import java.util.List;

public class HudView {
    private KnightModel knightModel;

    private Texture emptyHealth;
    private Texture filledHealth;


    private Texture refillSheet;
    private Animation<TextureRegion> refillAnimation;
    private float refillTimer = 0f;
    private int refillingIndex = -1;

    private Texture breakSheet;
    private Animation<TextureRegion> breakAnimation;
    private float breakTimer = 0f;
    private int breakingIndex = -1;

    private int lastHealth;

    private Animation<TextureRegion>[] soulAnimations;
    private float soulAnimationTimer = 0f;
    private List<Texture> loadedSoulTextures;

    public HudView(KnightModel knightModel) {
        this.knightModel = knightModel;
        this.lastHealth = knightModel.getCurrentHealth();

        emptyHealth = new Texture("animation/HUD/EmptyHealth.png");
        filledHealth = new Texture("animation/HUD/FilledHealth.png");

        breakSheet = new Texture("animation/HUD/BreakHealth.png");
        int frameCols = 6;
        TextureRegion[][] tmp = TextureRegion.split(breakSheet, breakSheet.getWidth() / frameCols, breakSheet.getHeight());
        TextureRegion[] breakFrames = new TextureRegion[frameCols];
        for (int i = 0; i < frameCols; i++) { breakFrames[i] = tmp[0][i]; }
        breakAnimation = new Animation<>(0.1f, breakFrames);

        refillSheet = new Texture("animation/HUD/HealthRefill.png");
        int refillCols = 5;
        TextureRegion[][] tmpRefill = TextureRegion.split(refillSheet, refillSheet.getWidth() / refillCols, refillSheet.getHeight());
        TextureRegion[] refillFrames = new TextureRegion[refillCols];
        for (int i = 0; i < refillCols; i++) { refillFrames[i] = tmpRefill[0][i]; }
        refillAnimation = new Animation<>(0.1f, refillFrames);



        loadedSoulTextures = new ArrayList<>();
        soulAnimations = new Animation[6];

        soulAnimations[0] = createAnimFromFiles(0.3f, "animation/HUD/HUD Cln_032.png");

        soulAnimations[1] = createAnimFromFiles(0.3f,
            "animation/HUD/HUD Cln_238.png",
            "animation/HUD/HUD Cln_239.png",
            "animation/HUD/HUD Cln_240.png",
            "animation/HUD/HUD Cln_241.png",
            "animation/HUD/HUD Cln_242.png");

        soulAnimations[2] = createAnimFromFiles(0.3f,
            "animation/HUD/HUD Cln_242.png",
            "animation/HUD/HUD Cln_243.png",
            "animation/HUD/HUD Cln_244.png");

        soulAnimations[3] = createAnimFromFiles(0.3f,
            "animation/HUD/HUD Cln_244.png",
            "animation/HUD/HUD Cln_245.png",
            "animation/HUD/HUD Cln_246.png",
            "animation/HUD/HUD Cln_247.png",
            "animation/HUD/HUD Cln_248.png");

        soulAnimations[4] = createAnimFromFiles(0.3f,
            "animation/HUD/HUD Cln_249.png",
            "animation/HUD/HUD Cln_250.png",
            "animation/HUD/HUD Cln_251.png",
            "animation/HUD/HUD Cln_252.png",
            "animation/HUD/HUD Cln_253.png",
            "animation/HUD/HUD Cln_254.png");

        soulAnimations[5] = createAnimFromFiles(0.3f, "animation/HUD/SoulOrb_Half.png");
    }

    private Animation<TextureRegion> createAnimFromFiles(float frameDuration, String... filePaths) {
        TextureRegion[] frames = new TextureRegion[filePaths.length];

        for (int i = 0; i < filePaths.length; i++) {
            Texture tex = new Texture(filePaths[i]);
            loadedSoulTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }

        return new Animation<>(frameDuration, frames);
    }

    private int getSoulFrameIndex(float currentSoul, float maxSoul) {
        float percentage = (currentSoul / maxSoul) * 100f;
        if (percentage >= 100f) return 5;
        else if (percentage >= 90f) return 4;
        else if (percentage >= 50f) return 3;
        else if (percentage >= 30f) return 2;
        else if (percentage >= 20f) return 1;
        else return 0;
    }

    public void render(SpriteBatch batch, float delta) {
        int currentHealth = knightModel.getCurrentHealth();
        int maxHealth = 5;

        if (currentHealth < lastHealth) {
            breakingIndex = currentHealth;
            breakTimer = 0f;
            lastHealth = currentHealth;
        } else if (currentHealth > lastHealth) {
            refillingIndex = lastHealth;
            refillTimer = 0f;
            lastHealth = currentHealth;
        }

        soulAnimationTimer += delta;
        int soulIndex = getSoulFrameIndex(knightModel.getCurrentSoul(), knightModel.getMaxSoul());

        TextureRegion currentVesselFrame = soulAnimations[soulIndex].getKeyFrame(soulAnimationTimer, true);

        if (soulIndex == 5) {
            batch.draw(currentVesselFrame, 45, 900, 150, 100);
        } else {
            batch.draw(currentVesselFrame, -10, 820, 180, 200);
        }


        float startX = 140;
        float startY = 890;
        float spacing = 50;

        for (int i = 0; i < maxHealth; i++) {
            float x = startX + (i * spacing);
            float y = startY;

            if (i == refillingIndex && !refillAnimation.isAnimationFinished(refillTimer)) {
                refillTimer += delta;
                TextureRegion currentFrame = refillAnimation.getKeyFrame(refillTimer, false);
                batch.draw(currentFrame, x, y, 80, 80);
            }
            else if (i == breakingIndex && !breakAnimation.isAnimationFinished(breakTimer)) {
                breakTimer += delta;
                TextureRegion currentFrame = breakAnimation.getKeyFrame(breakTimer, false);
                batch.draw(currentFrame, x, y, 80, 80);
            }
            else if (i < currentHealth) {
                batch.draw(filledHealth, x, y, 80, 80);
            }
            else {
                batch.draw(emptyHealth, x, y, 80, 80);
            }
        }
    }

    public void dispose() {
        emptyHealth.dispose();
        filledHealth.dispose();
        breakSheet.dispose();
        refillSheet.dispose();

        for (Texture tex : loadedSoulTextures) {
            tex.dispose();
        }
    }
}
