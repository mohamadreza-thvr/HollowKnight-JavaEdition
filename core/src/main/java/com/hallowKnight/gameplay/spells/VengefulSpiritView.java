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

    private Texture sheet;

    public VengefulSpiritView(KnightModel model) {
        if(model.isCharmEquipped(KnightModel.Charm.SHARP_SHADOW)) sheet = new Texture(Gdx.files.internal("animation/Projectile/SoulBall.png"));
        else sheet = new Texture(Gdx.files.internal("animation/Projectile/ShadowBall.png"));


        int frameWidth = sheet.getWidth() / 4;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, sheet.getHeight());
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = tmp[0][i];
        }

        soulBallAnimation = new Animation<>(0.1f, frames);
    }

    public void render(SpriteBatch batch, List<VengefulSpiritModel> activeSpells) {
        for (VengefulSpiritModel spell : activeSpells) {
            TextureRegion frame = soulBallAnimation.getKeyFrame(spell.getStateTime(), false);

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
        if (sheet != null) sheet.dispose();
    }
}
