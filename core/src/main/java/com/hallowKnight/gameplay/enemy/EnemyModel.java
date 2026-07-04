package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;

public abstract class EnemyModel {
    protected float x, y;
    protected float velocityX = 0, velocityY = 0;
    protected Rectangle bounds;

    protected int health;
    protected boolean isDead = false;
    protected boolean facingRight = false;

    protected boolean isKnockedBack = false;
    protected float knockbackTimer = 0f;

    protected boolean hitGroundAfterDeath = false;

    protected boolean isGrounded = false;

    public EnemyModel(float x, float y, float width, float height, int health) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, width, height);
        this.health = health;
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public void takeDamage(int amount, float sourceX, boolean isHeavyBlow) {
        if (isDead) return;

        health -= amount;

        float horizontalKnockback = isHeavyBlow ? 650f : 400f;

        if (health <= 0) {
            health = 0;
            isDead = true;
            velocityY = 300f;
        } else {
            isKnockedBack = true;
            knockbackTimer = 0.25f;
            velocityY = 200f;
        }

        if (x > sourceX) {
            velocityX = horizontalKnockback;
            facingRight = false;
        } else {
            velocityX = -horizontalKnockback;
            facingRight = true;
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; updateBounds(); }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; updateBounds(); }
    public float getVelocityX() { return velocityX; }
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }
    public float getVelocityY() { return velocityY; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public Rectangle getBounds() { return bounds; }
    public boolean isDead() { return isDead; }
    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }
    public boolean isKnockedBack() { return isKnockedBack; }
    public void setKnockedBack(boolean knockedBack) { this.isKnockedBack = knockedBack; }
    public float getKnockbackTimer() { return knockbackTimer; }
    public void setKnockbackTimer(float knockbackTimer) { this.knockbackTimer = knockbackTimer; }
    public boolean isHitGroundAfterDeath() { return hitGroundAfterDeath; }
    public void setHitGroundAfterDeath(boolean hitGroundAfterDeath) { this.hitGroundAfterDeath = hitGroundAfterDeath; }
    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean grounded) { this.isGrounded = grounded; }
}
