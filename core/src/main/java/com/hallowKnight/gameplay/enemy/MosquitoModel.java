package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Vector2;

public class MosquitoModel extends EnemyModel{

    private EnemyState currentState;
    private float stateTimer;

    private boolean hasAggro = false;
    private Vector2 targetPosition;

    public MosquitoModel(float x, float y) {
        super(x, y, 50, 50, 3);
        currentState = EnemyState.IDLE;
        stateTimer = 0;
        this.targetPosition = new Vector2();
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState state) { this.currentState = state; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float timer) { this.stateTimer = timer; }
    public boolean isHasAggro() { return hasAggro; }
    public void setHasAggro(boolean hasAggro) { this.hasAggro = hasAggro; }
    public Vector2 getTargetPosition() { return targetPosition; }
}
