package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;

public class HuskModel extends EnemyModel{

    private EnemyState currentState = EnemyState.IDLE;
    private float stateTimer = 0f;

    private Rectangle visionBounds;
    private boolean isAggro = false;



    public HuskModel(float x, float y) {
        super(x, y, 70, 90, 5);
        currentState = EnemyState.IDLE;
        stateTimer = 0f;
        facingRight = false;
        visionBounds = new Rectangle();
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState state) { this.currentState = state; }

    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float timer) { this.stateTimer = timer; }

    public boolean isAggro() { return isAggro; }
    public void setAggro(boolean aggro) { this.isAggro = aggro; }

    public Rectangle getVisionBounds() { return visionBounds; }
}
