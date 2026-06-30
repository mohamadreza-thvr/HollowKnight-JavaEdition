package com.hallowKnight.gameplay.spells;

import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.gameplay.enemy.EnemyModel;

import java.util.ArrayList;
import java.util.List;

public class HowlingWraithsModel {
    private float x, y;
    private Rectangle bounds;
    private boolean active = true;
    private float stateTime = 0f;
    private int currentHitTick = 0;

    private List<EnemyModel> hitEnemiesThisTick = new ArrayList<>();

    public HowlingWraithsModel(float knightX, float knightY, float knightWidth, float knightHeight) {
        float width = 220f;
        float height = 260f;

        this.x = knightX + (knightWidth - width) / 2f;
        this.y = knightY + knightHeight - 30f;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        if (!active) return;
        stateTime += delta;

        if (stateTime >= 0.65f) {
            active = false;
        }
        int expectedTick = (int)(stateTime / 0.216f);
        if (expectedTick > currentHitTick) {
            currentHitTick = expectedTick;
            hitEnemiesThisTick.clear();
        }
    }

    public boolean hasHit(EnemyModel enemy) { return hitEnemiesThisTick.contains(enemy); }
    public void addHitEnemy(EnemyModel enemy) { hitEnemiesThisTick.add(enemy); }

    public boolean isActive() { return active; }
    public void setActive(boolean active) {this.active = active;}

    public Rectangle getBounds() { return bounds; }
    public float getStateTime() { return stateTime; }
    public float getX() { return x; }
    public float getY() { return y; }
}

