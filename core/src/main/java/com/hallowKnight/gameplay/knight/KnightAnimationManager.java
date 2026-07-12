package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class KnightAnimationManager {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> fallAnimation;
    private Animation<TextureRegion> lookUpAnimation;
    private Animation<TextureRegion> lookDownAnimation;
    private Animation<TextureRegion> landingAnimation;
    private Animation<TextureRegion> wallSlideAnimation;
    private Animation<TextureRegion> wallJumpAnimation;
    private Animation<TextureRegion> doubleJumpAnimation;

    private Animation<TextureRegion> focusAnimation;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> sharpShadowDashAnimation;
    private Animation<TextureRegion> deathAnimation;

    private Animation<TextureRegion> blastEffectAnimation;

    private Animation<TextureRegion> slashAnimation;
    private Animation<TextureRegion> slashAltAnimation;
    private Animation<TextureRegion> upSlashAnimation;
    private Animation<TextureRegion> downSlashAnimation;

    private Animation<TextureRegion> dashEffectAnimation;
    private Animation<TextureRegion> slashEffectAnimation;
    private Animation<TextureRegion> slashEffectAltAnimation;
    private Animation<TextureRegion> upSlashEffectAnimation;
    private Animation<TextureRegion> downSlashEffectAnimation;

    private List<Texture> loadedTextures;
    private float stateTime = 0f;

    public KnightAnimationManager() {
        loadedTextures = new ArrayList<>();
        loadAnimations();
    }

    private void loadAnimations() {
        idleAnimation = createSpriteSheetAnimation("animation/idle.png", 9, 1, 0.1f);
        runAnimation = createSpriteSheetAnimation("animation/Run.png", 13, 1, 0.1f);
        jumpAnimation = createSpriteSheetAnimation("animation/Airborne.png", 12, 1, 0.1f);
        doubleJumpAnimation = createSpriteSheetAnimation("animation/Double Jump.png", 8, 1, 0.1f);

        String[] fallFiles = {"animation/Fall_000.png", "animation/Fall_001.png", "animation/Fall_002.png",
            "animation/Fall_003.png", "animation/Fall_004.png", "animation/Fall_005.png"};
        fallAnimation = createAnimationFromSeparateFiles(fallFiles, 0.1f);

        lookUpAnimation = createSpriteSheetAnimation("animation/LookUp.png", 6, 1, 0.1f);
        lookDownAnimation = createSpriteSheetAnimation("animation/LookDown.png", 6, 1, 0.1f);
        landingAnimation = createSpriteSheetAnimation("animation/Landing.png", 4, 1, 0.1f);

        String[] focusFiles = {"animation/Focus_001.png", "animation/Focus_002.png", "animation/Focus_003.png",
            "animation/Focus_004.png", "animation/Focus_005.png", "animation/Focus_006.png"};
        focusAnimation = createAnimationFromSeparateFiles(focusFiles, 0.1f);

        String[] blastFiles = {
            "animation/Projectile/Blast_000.png", "animation/Projectile/Blast_001.png",
            "animation/Projectile/Blast_002.png", "animation/Projectile/Blast_003.png",
            "animation/Projectile/Blast_004.png", "animation/Projectile/Blast_005.png",
            "animation/Projectile/Blast_006.png", "animation/Projectile/Blast_007.png"
        };
        blastEffectAnimation = createAnimationFromSeparateFiles(blastFiles, 0.05f);

        wallSlideAnimation = createSpriteSheetAnimation("animation/Wall Slide.png", 4, 1, 0.1f);
        wallJumpAnimation = createSpriteSheetAnimation("animation/Walljump.png", 9, 1, 0.1f);

        dashAnimation = createSpriteSheetAnimation("animation/Dash.png", 12, 1, 0.1f);
        deathAnimation = createSpriteSheetAnimation("animation/Death.png", 18, 1, 0.08f);
        sharpShadowDashAnimation = createSpriteSheetAnimation("animation/Shadow Dash.png", 11, 1, 0.15f);

        slashAnimation = createSpriteSheetAnimation("animation/Slash.png", 5, 1, 0.08f);
        slashAltAnimation = createSpriteSheetAnimation("animation/SlashAlt.png", 5, 1, 0.08f);
        upSlashAnimation = createSpriteSheetAnimation("animation/UpSlash.png", 5, 1, 0.08f);
        downSlashAnimation = createSpriteSheetAnimation("animation/DownSlash.png", 5, 1, 0.08f);

        dashEffectAnimation = createSpriteSheetAnimation("animation/Effects/Dash Effect.png", 8, 1, 0.05f);
        slashEffectAnimation = createSpriteSheetAnimation("animation/Effects/SlashEffect.png", 6, 1, 0.08f);
        slashEffectAltAnimation = createSpriteSheetAnimation("animation/Effects/SlashEffectAlt.png", 6, 1, 0.08f);
        upSlashEffectAnimation = createSpriteSheetAnimation("animation/Effects/UpSlashEffect.png", 6, 1, 0.08f);
        downSlashEffectAnimation = createSpriteSheetAnimation("animation/Effects/DownSlashEffect.png", 6, 1, 0.08f);
    }

    private Animation<TextureRegion> createAnimationFromSeparateFiles(String[] fileNames, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            Texture tex = new Texture(Gdx.files.internal(fileNames[i]));
            loadedTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }

    private Animation<TextureRegion> createSpriteSheetAnimation(String fileName, int columns, int rows, float frameDuration) {
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

    public TextureRegion getCurrentFrame(float delta, KnightModel model) {
        stateTime += delta;
        Animation<TextureRegion> currentAnim = idleAnimation;
        float animationTime = stateTime;
        boolean loop = true;

        if (model.isDead()) {
            currentAnim = deathAnimation;
            animationTime = model.getDeathTimer();
            loop = false;
        }
        else if (model.isCasting()) {
            if (model.isCastingUp()) {
                currentAnim = lookUpAnimation;
                animationTime = 0.65f - model.getCastTimer();
            } else {
                currentAnim = focusAnimation;
                animationTime = 0.4f - model.getCastTimer();
            }
            loop = false;
        }
        else if (model.isDashing()) {
            if(model.isCharmEquipped(KnightModel.Charm.SHARP_SHADOW)){
                currentAnim = sharpShadowDashAnimation;
            } else {
                currentAnim = dashAnimation;
            }
        }
        else if (model.isAttacking()) {
            if (model.getAttackDirection() == 1) {
                currentAnim = upSlashAnimation;
            } else if (model.getAttackDirection() == -1) {
                currentAnim = downSlashAnimation;
            } else {
                currentAnim = model.isAltSlash() ? slashAltAnimation : slashAnimation;
            }
            animationTime = model.getAttackTimer();
        }
        else if (model.isFocusing()) {
            currentAnim = focusAnimation;
        }
        else if (model.isWallSliding()) {
            currentAnim = wallSlideAnimation;
        }
        else if (model.getWallJumpTimer() > 0) {
            currentAnim = wallJumpAnimation;
        }
        else if (!model.isGrounded()) {
            if (model.isDoubleJumping() && model.getVelocityY() > 0) {
                currentAnim = doubleJumpAnimation;
            } else if (model.getVelocityY() > 0) {
                currentAnim = jumpAnimation;
            } else {
                currentAnim = fallAnimation;
            }
        }
        else {
            if (model.getVelocityX() != 0) {
                currentAnim = runAnimation;
            } else if (model.isLookingUp()) {
                currentAnim = lookUpAnimation;
                loop = false;
            } else if (model.isLookingDown()) {
                currentAnim = lookDownAnimation;
                loop = false;

            } else if (model.isLanding()) {
                currentAnim = landingAnimation;
                loop = false;
            } else {
                currentAnim = idleAnimation;
            }
        }

        TextureRegion currentFrame = currentAnim.getKeyFrame(animationTime, loop);

        boolean facingRight = model.isFacingRight();
        if (currentFrame.isFlipX() != facingRight) {
            currentFrame.flip(true, false);
        }
        return currentFrame;
    }

    public Animation<TextureRegion> getDashEffectAnimation() { return dashEffectAnimation; }
    public Animation<TextureRegion> getSlashEffectAnimation() { return slashEffectAnimation; }
    public Animation<TextureRegion> getSlashEffectAltAnimation() { return slashEffectAltAnimation; }
    public Animation<TextureRegion> getUpSlashEffectAnimation() { return upSlashEffectAnimation; }
    public Animation<TextureRegion> getDownSlashEffectAnimation() { return downSlashEffectAnimation; }

    public TextureRegion getBlastEffectFrame(float stateTime, boolean facingRight) {
        TextureRegion frame = blastEffectAnimation.getKeyFrame(stateTime, false);
        if (frame.isFlipX() == facingRight) {
            frame.flip(true, false);
        }
        return frame;
    }

    public void dispose() {
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
    }
}
