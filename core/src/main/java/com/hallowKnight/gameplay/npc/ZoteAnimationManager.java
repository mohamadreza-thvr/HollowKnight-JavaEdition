package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.List;

public class ZoteAnimationManager {
    private List<Texture> loadedTextures;

    private Animation<TextureRegion> idle, talk, turn, attack, fall, getUp, roll;
    private float stateTime = 0f;

    public ZoteAnimationManager() {
        loadedTextures = new ArrayList<>();
        idle = createSpriteSheet("animation/Zote/Idle.png", 5, 0.3f);
        talk = createSpriteSheet("animation/Zote/Talk.png", 5, 0.1f);
        turn = createSpriteSheet("animation/Zote/Turn.png", 2, 0.2f);
        attack = createSpriteSheet("animation/Zote/Attack.png", 4, 0.15f);
        fall = createSpriteSheet("animation/Zote/Fall.png", 5, 0.3f);
        getUp = createSpriteSheet("animation/Zote/Get Up.png", 4, 0.3f);
        roll = createSpriteSheet("animation/Zote/Roll.png", 3, 0.1f);
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

    public TextureRegion getCurrentFrame(float delta, ZoteModel zote) {
        stateTime += delta;
        Animation<TextureRegion> currentAnim = idle;
        boolean loop = true;

        if (zote.getCurrentState() == ZoteState.ATTACK) { currentAnim = attack; }
        else if (zote.getCurrentState() == ZoteState.TALK) { currentAnim = talk; }
        else if (zote.getCurrentState() == ZoteState.FALL) { currentAnim = fall; loop = false; }
        else if (zote.getCurrentState() == ZoteState.GET_UP) { currentAnim = getUp; loop = false; }
        else if (zote.getCurrentState() == ZoteState.TURN) { currentAnim = turn; loop = false; }
        else if (zote.getCurrentState() == ZoteState.ROLL) { currentAnim = roll; }

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, loop);
        if (frame.isFlipX() != zote.isFacingRight()) {
            frame.flip(true, false);
        }
        return frame;
    }

    public void dispose() {
        for (Texture tex : loadedTextures) tex.dispose();
    }
}
