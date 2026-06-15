package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.math.Rectangle;

public class KnightModel {
    private float x, y;
    private float width = 40, height = 60;
    private float velocityX = 0, velocityY = 0;

    // متغیرهای فیزیکی که قبلا صحبت کردیم
    private float speed = 350f;       // سرعت حرکت
    private float jumpForce = 800f;   // قدرت پرش
    private float gravity = 2000f;    // جاذبه

    private boolean isGrounded = false;
    private boolean isFacingRight = true;
    private Rectangle bounds;

    public KnightModel(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    // --- Getter / Setter ---
    public float getX() { return x; }
    public void setX(float x) { this.x = x; updateBounds(); }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; updateBounds(); }

    public float getVelocityX() { return velocityX; }
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }

    public float getVelocityY() { return velocityY; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }

    public float getSpeed() { return speed; }
    public float getJumpForce() { return jumpForce; }
    public float getGravity() { return gravity; }

    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean grounded) { isGrounded = grounded; }

    public boolean isFacingRight() { return isFacingRight; }
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }

    public Rectangle getBounds() { return bounds; }
}
