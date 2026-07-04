package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;

public class CrystalCrawlerModel extends EnemyModel {
    private EnemyState currentState = EnemyState.WALK;
    private float stateTimer = 0f;

    private Rectangle platform = null;
    private int edge = 0;
    private float rotation = 0f;

    public CrystalCrawlerModel(float x, float y) {
        super(x, y, 70, 90, 3);
        this.facingRight = true;
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState currentState) { this.currentState = currentState; }

    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }

    public Rectangle getPlatform() { return platform; }
    public void setPlatform(Rectangle platform) { this.platform = platform; }

    public int getEdge() { return edge; }
    public void setEdge(int edge) { this.edge = edge; }

    public float getRotation() { return rotation; }
    public void setRotation(float rotation) { this.rotation = rotation; }
}
