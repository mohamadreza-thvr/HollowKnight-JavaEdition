package com.hallowKnight.gameplay.boss;

import com.badlogic.gdx.math.Rectangle;

public class ShockwaveModel {
    private float x, y;
    private float velocityX;
    private Rectangle bounds;
    private float stateTime;
    private boolean facingRight;

    public ShockwaveModel(float x, float y, boolean facingRight) {
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;

        this.velocityX = facingRight ? 300f : -300f;

        this.bounds = new Rectangle(x, y, 60, 80);
        this.stateTime = 0f;
    }

    public void update(float delta) {
        float acceleration = facingRight ? 250f : -250f;
        velocityX += acceleration * delta;

        x += velocityX * delta;
        bounds.setPosition(x, y);
        stateTime += delta;
    }

    public Rectangle getBounds() { return bounds; }
    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isFacingRight() { return facingRight; }
    public float getStateTime() { return stateTime; }
}
