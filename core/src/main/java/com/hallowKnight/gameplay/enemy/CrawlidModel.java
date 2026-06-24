package com.hallowKnight.gameplay.enemy;

public class CrawlidModel extends EnemyModel{
    private EnemyState currentState;
    private float stateTimer;

    public CrawlidModel(float x, float y) {
        super(x, y, 60, 40, 2);
        currentState = EnemyState.WALK;
        stateTimer = 0f;
        facingRight = false;
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState state) { this.currentState = state; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }
}
