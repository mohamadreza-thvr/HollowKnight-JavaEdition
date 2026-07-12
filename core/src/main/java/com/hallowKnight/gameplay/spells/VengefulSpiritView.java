package com.hallowKnight.gameplay.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hallowKnight.gameplay.knight.KnightModel;

import java.util.List;

public class VengefulSpiritView {
    private Animation<TextureRegion> soulBallAnimation;
    private Animation<TextureRegion> shadowBallAnimation;

    private Texture soulSheet;
    private Texture shadowSheet;
    private KnightModel knightModel;

    public VengefulSpiritView(KnightModel model) {
        this.knightModel = model;

        soulSheet = new Texture(Gdx.files.internal("animation/Projectile/SoulBall.png"));
        TextureRegion[][] tmpSoul = TextureRegion.split(soulSheet, soulSheet.getWidth() / 4, soulSheet.getHeight());
        TextureRegion[] soulFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            soulFrames[i] = tmpSoul[0][i];
        }
        soulBallAnimation = new Animation<>(0.1f, soulFrames);

        shadowSheet = new Texture(Gdx.files.internal("animation/Projectile/ShadowBall.png"));
        TextureRegion[][] tmpShadow = TextureRegion.split(shadowSheet, shadowSheet.getWidth() / 6, shadowSheet.getHeight());
        TextureRegion[] shadowFrames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            shadowFrames[i] = tmpShadow[0][i];
        }
        shadowBallAnimation = new Animation<>(0.1f, shadowFrames);
    }

    public void render(SpriteBatch batch, List<VengefulSpiritModel> activeSpells) {
        boolean isVoid = knightModel.isCharmEquipped(KnightModel.Charm.VOID_HEART);
        Animation<TextureRegion> currentAnim = isVoid ? shadowBallAnimation : soulBallAnimation;

        for (VengefulSpiritModel spell : activeSpells) {
            TextureRegion frame = currentAnim.getKeyFrame(spell.getStateTime(), true);

            if (frame.isFlipX() == spell.isFacingRight()) {
                frame.flip(true, false);
            }

            float width = 140f;
            float height = 90f;

            float offsetX = (width - spell.getBounds().width) / 2f;
            float offsetY = (height - spell.getBounds().height) / 2f;

            batch.draw(frame, spell.getX() - offsetX, spell.getY() - offsetY, width, height);
        }
    }

    public void dispose() {
        if (soulSheet != null) soulSheet.dispose();
        if (shadowSheet != null) shadowSheet.dispose();
    }
}
