package com.hallowKnight.gameplay.spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.List;

public class HowlingWraithsView {
    private Animation<TextureRegion> screamAnimation;
    private List<Texture> loadedTextures = new ArrayList<>();

    public HowlingWraithsView() {
        TextureRegion[] frames = new TextureRegion[13];
        for (int i = 0; i < 13; i++) {
            String path = String.format("animation/Effects/SoulScream_%03d.png", i);
            Texture tex = new Texture(Gdx.files.internal(path));
            loadedTextures.add(tex);
            frames[i] = new TextureRegion(tex);
        }
        screamAnimation = new Animation<>(0.05f, frames);
    }

    public void render(SpriteBatch batch, List<HowlingWraithsModel> wraiths) {
        for (HowlingWraithsModel wraith : wraiths) {
            TextureRegion frame = screamAnimation.getKeyFrame(wraith.getStateTime(), false);
            batch.draw(frame, wraith.getX(), wraith.getY(), wraith.getBounds().width, wraith.getBounds().height);
        }
    }

    public void dispose() {
        for (Texture t : loadedTextures) t.dispose();
    }
}
