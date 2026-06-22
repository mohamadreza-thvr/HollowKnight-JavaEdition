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

    // انیمیشن جدید تمرکز
    private Animation<TextureRegion> focusAnimation;

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

        String[] fallFiles = {"animation/Fall_000.png", "animation/Fall_001.png", "animation/Fall_002.png",
            "animation/Fall_003.png", "animation/Fall_004.png", "animation/Fall_005.png"};
        fallAnimation = createAnimationFromSeparateFiles(fallFiles, 0.1f);

        lookUpAnimation = createSpriteSheetAnimation("animation/LookUp.png", 6, 1, 0.1f);
        lookDownAnimation = createSpriteSheetAnimation("animation/LookDown.png", 6, 1, 0.1f);
        landingAnimation = createSpriteSheetAnimation("animation/Landing.png", 4, 1, 0.1f);

        String[] focusFiles = {
            "animation/Focus_001.png",
            "animation/Focus_002.png",
            "animation/Focus_003.png",
            "animation/Focus_004.png",
            "animation/Focus_005.png",
            "animation/Focus_006.png"
        };
        focusAnimation = createAnimationFromSeparateFiles(focusFiles, 0.1f);
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
        Animation<TextureRegion> currentAnim;
        boolean loop = true;

        if (model.isFocusing()) {
            currentAnim = focusAnimation;
        }
        else if (!model.isGrounded()) {
            if (model.getVelocityY() > 0) {
                currentAnim = jumpAnimation;
            } else {
                currentAnim = fallAnimation;
            }
        } else {
            if (model.getVelocityX() != 0) {
                currentAnim = runAnimation;
            } else if (model.isLookingUp()) {
                currentAnim = lookUpAnimation;
            } else if (model.isLookingDown()) {
                currentAnim = lookDownAnimation;
            } else if (model.isLanding()) {
                currentAnim = landingAnimation;
                loop = false;
            } else {
                currentAnim = idleAnimation;
            }
        }

        TextureRegion currentFrame = currentAnim.getKeyFrame(stateTime, loop);

        boolean facingRight = model.isFacingRight();
        if (currentFrame.isFlipX() != facingRight) {
            currentFrame.flip(true, false);
        }
        return currentFrame;
    }

    public void dispose() {
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
    }
}
