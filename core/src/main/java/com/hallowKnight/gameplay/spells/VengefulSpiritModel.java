package com.hallowKnight.gameplay.spells;

import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.gameplay.enemy.EnemyModel;
import java.util.ArrayList;
import java.util.List;

public class VengefulSpiritModel {
    private float x, y;
    private float speed = 800f;
    private boolean facingRight;
    private Rectangle bounds;
    private boolean active = true;
    private float stateTime = 0f;
    private float lifeTimer = 2.0f;

    private List<EnemyModel> hitEnemies = new ArrayList<>();

    public VengefulSpiritModel(float startX, float startY, boolean facingRight) {
        this.x = startX;
        this.y = startY;
        this.facingRight = facingRight;
        this.bounds = new Rectangle(x, y, 100, 60);
    }

    public void update(float delta) {
        if (!active) return;

        stateTime += delta;
        lifeTimer -= delta;
        if (lifeTimer <= 0) active = false;

        x += (facingRight ? speed : -speed) * delta;
        bounds.setPosition(x, y);
    }

    public boolean hasHit(EnemyModel enemy) {
        return hitEnemies.contains(enemy);
    }

    public void addHitEnemy(EnemyModel enemy) {
        hitEnemies.add(enemy);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public boolean isFacingRight() { return facingRight; }
    public float getStateTime() { return stateTime; }
}
