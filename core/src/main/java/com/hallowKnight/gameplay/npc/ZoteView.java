package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ZoteView {
    private ZoteAnimationManager animationManager;
    private BitmapFont font;
    private GlyphLayout layout;

    private static final float VISUAL_WIDTH = 140f;
    private static final float VISUAL_HEIGHT = 140f;
    private static final float DIALOGUE_OFFSET_Y = 60f;
    private static final float DIALOGUE_LINE_SPACING = 35f;

    public ZoteView() {
        animationManager = new ZoteAnimationManager();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();
    }

    public void renderWorld(SpriteBatch batch, float delta, ZoteModel zote) {
        if (zote == null) return;

        TextureRegion frame = animationManager.getCurrentFrame(delta, zote);

        float offsetX = (VISUAL_WIDTH - zote.getBounds().width) / 2f;

        batch.draw(frame, zote.getX() - offsetX, zote.getY() - 20, VISUAL_WIDTH, VISUAL_HEIGHT);

        if (zote.isPlayerInRange() && !zote.isTalking() && zote.getCurrentState() == ZoteState.IDLE) {
            font.setColor(Color.WHITE);
            font.draw(batch, "UP: Talk", zote.getX() - 10, zote.getY() + 150);
        }

        if (zote.isTalking()) {
            renderDialogueAboveHead(batch, zote);
        }
    }

    private void renderDialogueAboveHead(SpriteBatch batch, ZoteModel zote) {
        float centerX = zote.getX() + zote.getBounds().width / 2f;
        float baseY = zote.getY() + VISUAL_HEIGHT + DIALOGUE_OFFSET_Y;

        font.setColor(Color.GRAY);
        drawCentered(batch, "Zote The Mighty:", centerX, baseY + DIALOGUE_LINE_SPACING * 2);

        font.setColor(Color.WHITE);
        drawCentered(batch, zote.getCurrentDisplayLine(), centerX, baseY + DIALOGUE_LINE_SPACING);

        font.setColor(Color.YELLOW);
        drawCentered(batch, "[ENTER]", centerX, baseY - 20f);
    }

    private void drawCentered(SpriteBatch batch, String text, float centerX, float y) {
        layout.setText(font, text);
        font.draw(batch, text, centerX - layout.width / 2f, y);
    }

    public void dispose() {
        animationManager.dispose();
        font.dispose();
    }
}
