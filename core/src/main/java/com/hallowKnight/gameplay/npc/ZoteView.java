package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ZoteView {
    private ZoteAnimationManager animationManager;
    private BitmapFont font;

    public ZoteView() {
        animationManager = new ZoteAnimationManager();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }

    public void renderWorld(SpriteBatch batch, float delta, ZoteModel zote) {
        if (zote == null) return;

        TextureRegion frame = animationManager.getCurrentFrame(delta, zote);

        float visualWidth = 140;
        float visualHeight = 140;
        float offsetX = (visualWidth - zote.getBounds().width) / 2f;

        batch.draw(frame, zote.getX() - offsetX, zote.getY(), visualWidth, visualHeight);

        if (zote.isPlayerInRange() && !zote.isTalking() && zote.getCurrentState() == ZoteModel.State.IDLE) {
            font.setColor(Color.WHITE);
            font.draw(batch, "UP: Talk", zote.getX() - 10, zote.getY() + 150);
        }
    }

    public void renderUI(SpriteBatch batch, ShapeRenderer shape, ZoteModel zote) {
        if (zote == null || !zote.isTalking()) return;

        float boxWidth = 800f;
        float boxHeight = 150f;
        float boxX = (1280 - boxWidth) / 2f;
        float boxY = 50f;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.8f);
        shape.rect(boxX, boxY, boxWidth, boxHeight);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.WHITE);
        shape.rect(boxX, boxY, boxWidth, boxHeight);
        shape.end();

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Zote The Mighty:", boxX + 20, boxY + boxHeight - 20);
        font.draw(batch, zote.getCurrentDisplayLine(), boxX + 40, boxY + boxHeight - 70);
        font.setColor(Color.YELLOW);
        font.draw(batch, "[ENTER]", boxX + boxWidth - 100, boxY + 30);
        batch.end();
    }

    public void dispose() {
        animationManager.dispose();
        font.dispose();
    }
}
