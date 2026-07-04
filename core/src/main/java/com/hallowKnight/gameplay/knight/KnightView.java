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

        if (model.isAttacking()) {
            TextureRegion effectFrame = null;

            if (model.getAttackDirection() == 1) {
                effectFrame = animationManager.getUpSlashEffectAnimation().getKeyFrame(model.getAttackTimer(), false);
            } else if (model.getAttackDirection() == -1) {
                effectFrame = animationManager.getDownSlashEffectAnimation().getKeyFrame(model.getAttackTimer(), false);
            } else {
                effectFrame = model.isAltSlash()
                    ? animationManager.getSlashEffectAltAnimation().getKeyFrame(model.getAttackTimer(), false)
                    : animationManager.getSlashEffectAnimation().getKeyFrame(model.getAttackTimer(), false);
            }

            if (effectFrame != null) {
                if (effectFrame.isFlipX() != model.isFacingRight()) {
                    effectFrame.flip(true, false);
                }

                batch.draw(effectFrame,
                    model.getX() - offsetX,
                    model.getY() - offsetY,
                    visualWidth,
                    visualHeight);
            }
        }

        if (model.isDashing()) {
            float elapsedTime = Math.max(0f, 0.25f - model.getDashTimer());

            TextureRegion dashEffectFrame = animationManager.getDashEffectAnimation().getKeyFrame(elapsedTime, false);

            if (dashEffectFrame.isFlipX() == model.isFacingRight()) {
                dashEffectFrame.flip(true, false);
            }

            int dashSide;
            if(model.isCharmEquipped(KnightModel.Charm.SHARP_SHADOW)) dashSide = model.isFacingRight() ? 1 : -1;
            dashSide = model.isFacingRight() ? 1 : -1;

            batch.draw(dashEffectFrame,
                model.getX() - offsetX - dashSide * 150,
                model.getY() - offsetY - 20,
                visualWidth,
                visualHeight);
        }

        if (model.isCasting() && !model.isCastingUp()) {
            float castStateTime = 0.6f - model.getCastTimer();
            TextureRegion blastFrame = animationManager.getBlastEffectFrame(castStateTime, model.isFacingRight());

            float blastWidth = 160f;
            float blastHeight = 120f;
            float blastX = model.isFacingRight() ? model.getX() + 30f : model.getX() - blastWidth + 30f;
            float blastY = model.getY() - 10f;

            batch.draw(blastFrame, blastX, blastY, blastWidth, blastHeight);
        }
    }

    public void dispose() {
        animationManager.dispose();
    }
}
