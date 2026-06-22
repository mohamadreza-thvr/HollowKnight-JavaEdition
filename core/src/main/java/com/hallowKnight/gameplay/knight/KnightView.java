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
        if (model.isInvulnerable()) {
            if ((int)(model.getInvulnerabilityTimer() * 15) % 2 == 0) {
                return;
            }
        }

        TextureRegion currentFrame = animationManager.getCurrentFrame(delta, model);

        float visualWidth = 250;
        float visualHeight = 135;
        float offsetX = (visualWidth - model.getBounds().width) / 2f;
        float offsetY = 0;

        batch.draw(currentFrame,
            model.getX() - offsetX,
            model.getY() - offsetY,
            visualWidth,
            visualHeight);
    }

    public void dispose() {
        animationManager.dispose();
    }
}
