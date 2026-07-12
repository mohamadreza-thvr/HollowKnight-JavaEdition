package com.hallowKnight.gameplay.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hallowKnight.gameplay.knight.KnightModel;
import java.util.ArrayList;
import java.util.List;

public class HowlingWraithsView {
    private Animation<TextureRegion> screamAnimation;
    private Animation<TextureRegion> abyssShriekAnimation;
    private List<Texture> loadedTextures = new ArrayList<>();
    private KnightModel knightModel;

    public HowlingWraithsView(KnightModel model) {
        this.knightModel = model;

        TextureRegion[] frames = new TextureRegion[13];
        for (int i = 0; i < 13; i++) {
            String path = String.format("animation/Effects/SoulScream_%03d.png", i);
            Texture tex = new Texture(Gdx.files.internal(path));
            loadedTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }
        screamAnimation = new Animation<>(0.05f, frames);

        TextureRegion[] voidFrames = new TextureRegion[13];
        for (int i = 0; i < 13; i++) {

            String path = String.format("animation/Effects/ShadowScream_%03d.png", i);
            Texture tex = new Texture(Gdx.files.internal(path));
            loadedTextures.add(tex);
            voidFrames[i] = new TextureRegion(tex);
        }
        abyssShriekAnimation = new Animation<>(0.05f, voidFrames);
    }

    public void render(SpriteBatch batch, List<HowlingWraithsModel> wraiths) {
        boolean isVoid = knightModel.isCharmEquipped(KnightModel.Charm.VOID_HEART);
        Animation<TextureRegion> currentAnim = isVoid ? abyssShriekAnimation : screamAnimation;

        for (HowlingWraithsModel wraith : wraiths) {
            TextureRegion frame = currentAnim.getKeyFrame(wraith.getStateTime(), false);
            batch.draw(frame, wraith.getX(), wraith.getY(), wraith.getBounds().width, wraith.getBounds().height);
        }
    }

    public void dispose() {
        for (Texture t : loadedTextures) t.dispose();
    }
}
