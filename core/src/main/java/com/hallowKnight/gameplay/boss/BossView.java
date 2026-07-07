package com.hallowKnight.gameplay.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BossView {

    private static final float SPRITE_WIDTH  = 700f;
    private static final float SPRITE_HEIGHT = 700f;

    private final BossAnimationManager animManager;

    public BossView() {
        this.animManager = new BossAnimationManager();
    }

    public void render(SpriteBatch batch, BossModel boss, float delta) {
        if (boss == null) return;

        TextureRegion frame = animManager.getCurrentFrame(boss, delta);
        if (frame == null) return;


        boolean flip = !boss.isFacingRight();

        if (flip == frame.isFlipX()) {
            frame.flip(true, false);
        }

        batch.draw(frame, boss.getX()  - 200 , boss.getY() - 50 , SPRITE_WIDTH , SPRITE_HEIGHT);
        for (ShockwaveModel sw : boss.getShockwaves()) {
            TextureRegion swFrame = animManager.getShockwaveFrame(sw.getStateTime(), sw.isFacingRight());
            batch.draw(swFrame, sw.getX() - 20f, sw.getY() - 20f, 120f, 200);
        }
    }

    public void dispose() {
        animManager.dispose();
    }
}
