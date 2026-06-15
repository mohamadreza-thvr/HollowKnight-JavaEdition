package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.math.Rectangle;

public class KnightModel {
    private float x, y;
    private float width = 250, height = 135;
    private float velocityX = 0, velocityY = 0;


    private float speed = 350f;       // سرعت حرکت
    private float jumpForce = 800f;   // قدرت پرش
    private float gravity = 2000f;    // جاذبه

    private boolean isGrounded = false;
    private boolean isFacingRight = true;
    private Rectangle bounds;

    private boolean lookingUp = false;
    private boolean lookingDown = false;
    private boolean isLanding = false;
    private float landingTimer = 0f;
    private boolean wasGrounded = false; // برای تشخیص زمین خوردن


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

    public boolean isLookingUp() { return lookingUp; }
    public void setLookingUp(boolean lookingUp) { this.lookingUp = lookingUp; }

    public boolean isLookingDown() { return lookingDown; }
    public void setLookingDown(boolean lookingDown) { this.lookingDown = lookingDown; }

    public boolean isLanding() { return isLanding; }
    public void setLanding(boolean landing) { this.isLanding = landing; }

    public float getLandingTimer() { return landingTimer; }
    public void setLandingTimer(float landingTimer) { this.landingTimer = landingTimer; }

    public boolean wasGrounded() { return wasGrounded; }
    public void setWasGrounded(boolean wasGrounded) { this.wasGrounded = wasGrounded; }
}
