package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;

public class KnightModel {

    private int maxHealth = 5;
    private int currentHealth = 5;
    private boolean isInvulnerable = false;
    private float invulnerabilityTimer = 0f;

    private float maxSoul = 100f;
    private float currentSoul = 100f;

    private boolean isFocusing = false;
    private float focusTimer = 0f;

    private float lastSafeX, lastSafeY;

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

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int health) { this.currentHealth = health; }

    public boolean isInvulnerable() { return isInvulnerable; }
    public void setInvulnerable(boolean invulnerable) { this.isInvulnerable = invulnerable; }

    public float getInvulnerabilityTimer() { return invulnerabilityTimer; }
    public void setInvulnerabilityTimer(float timer) { this.invulnerabilityTimer = timer; }

    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }
    public void setSafePosition(float x, float y) { this.lastSafeX = x; this.lastSafeY = y; }

    public float getCurrentSoul() { return currentSoul; }
    public void setCurrentSoul(float soul) { this.currentSoul = Math.max(0, Math.min(soul, maxSoul)); }
    public float getMaxSoul() { return maxSoul; }

    public boolean isFocusing() { return isFocusing; }
    public void setFocusing(boolean focusing) { isFocusing = focusing; }

    public float getFocusTimer() { return focusTimer; }
    public void setFocusTimer(float focusTimer) { this.focusTimer = focusTimer; }

    public void heal(int amount) {
        if (currentHealth < 5) {
            currentHealth += amount;
        }
    }

    public void takeDamage(int amount) {
        if (!isInvulnerable) {
            currentHealth -= amount;
            if (currentHealth < 0) currentHealth = 0;
            isInvulnerable = true;
            invulnerabilityTimer = 1.5f;
        }
    }
}
