package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;

public class CrystalProjectileModel {
    private float x, y;
    private float velocityX, velocityY;
    private Rectangle bounds;

    public CrystalProjectileModel(float x, float y, float dirX, float dirY) {
        this.x = x;
        this.y = y;
        float speed = 400f;
        this.velocityX = dirX * speed;
        this.velocityY = dirY * speed;
        this.bounds = new Rectangle(x, y, 150, 150);
    }

    public void update(float delta) {
        x += velocityX * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
}
