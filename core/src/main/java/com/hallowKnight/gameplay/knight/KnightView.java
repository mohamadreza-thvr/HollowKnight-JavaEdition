package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KnightView {
    private KnightModel model;
    private KnightAnimationManager animationManager;

    public KnightView(KnightModel model) {
        this.model = model;
        this.animationManager = new KnightAnimationManager();
    }

    public void render(SpriteBatch batch, float delta) {
        TextureRegion currentFrame = animationManager.getCurrentFrame(delta, model);

        batch.draw(currentFrame, model.getX(), model.getY(), model.getBounds().width, model.getBounds().height);
    }

    public void dispose() {
        animationManager.dispose();
    }
}
