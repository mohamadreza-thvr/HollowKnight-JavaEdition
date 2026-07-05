package com.hallowKnight.gameplay.enemy;

public class CrystalHunterModel extends EnemyModel {
    private EnemyState currentState = EnemyState.IDLE;
    private float stateTimer = 0f;
    private boolean hasAggro = false;
    private float attackCooldown = 0f;

    public CrystalHunterModel(float x, float y) {
        super(x, y, 70, 70, 3);
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState currentState) { this.currentState = currentState; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }
    public boolean isHasAggro() { return hasAggro; }
    public void setHasAggro(boolean hasAggro) { this.hasAggro = hasAggro; }
    public float getAttackCooldown() { return attackCooldown; }
    public void setAttackCooldown(float attackCooldown) { this.attackCooldown = attackCooldown; }
}
