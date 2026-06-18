package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;

public class KnightModel {
    private float x, y;
    private float velocityX = 0, velocityY = 0;

    private boolean isGrounded = false;
    private boolean isFacingRight = true;
    private Rectangle bounds;

    private boolean lookingUp = false;
    private boolean lookingDown = false;
    private boolean isLanding = false;
    private float landingTimer = 0f;
    private boolean wasGrounded = false;

    private boolean isJumping = false;
    private float jumpTimer = 0f;

    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { isJumping = jumping; }

    public float getJumpTimer() { return jumpTimer; }
    public void setJumpTimer(float jumpTimer) { this.jumpTimer = jumpTimer; }


    public KnightModel(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.bounds = new Rectangle(x, y, Constance.KNIGHT_WIDTH, Constance.KNIGHT_HEIGHT);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }


    public float getX() { return x; }
    public void setX(float x) { this.x = x; updateBounds(); }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; updateBounds(); }

    public float getVelocityX() { return velocityX; }
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }

    public float getVelocityY() { return velocityY; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }

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
