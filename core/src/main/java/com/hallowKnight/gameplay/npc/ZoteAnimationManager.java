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
        idle = createSpriteSheet("animation/Zote/Idle.png", 5, 0.1f);
        talk = createSpriteSheet("animation/Zote/Talk.png", 5, 0.1f);
        turn = createSpriteSheet("animation/Zote/Turn.png", 2, 0.1f);
        attack = createSpriteSheet("animation/Zote/Attack.png", 4, 0.1f);
        fall = createSpriteSheet("animation/Zote/Fall.png", 5, 0.1f);
        getUp = createSpriteSheet("animation/Zote/Get Up.png", 4, 0.1f);
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

        if (zote.getCurrentState() == ZoteModel.State.ATTACK) { currentAnim = attack; }
        else if (zote.getCurrentState() == ZoteModel.State.TALK) { currentAnim = talk; }
        else if (zote.getCurrentState() == ZoteModel.State.FALL) { currentAnim = fall; loop = false; }
        else if (zote.getCurrentState() == ZoteModel.State.GET_UP) { currentAnim = getUp; loop = false; }
        else if (zote.getCurrentState() == ZoteModel.State.TURN) { currentAnim = turn; loop = false; }
        else if (zote.getCurrentState() == ZoteModel.State.ROLL) { currentAnim = roll; }

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
