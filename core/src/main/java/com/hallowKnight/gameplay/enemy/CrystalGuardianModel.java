package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;

public class CrystalGuardianModel extends EnemyModel {
    private EnemyState currentState = EnemyState.IDLE;
    private float stateTimer = 0f;
    private float enrageTimer = 0f;

    private boolean laserFiredThisTurn = false;
    private Rectangle visionBounds;
    private Rectangle laserBounds;
    private float laserLength = 0f;

    public CrystalGuardianModel(float x, float y) {
        super(x, y, 80, 100, 6);
        visionBounds = new Rectangle();
        laserBounds = new Rectangle();
        this.facingRight = false;
    }

    public EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(EnemyState currentState) { this.currentState = currentState; }

    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }

    public float getEnrageTimer() { return enrageTimer; }
    public void setEnrageTimer(float enrageTimer) { this.enrageTimer = enrageTimer; }

    public boolean isLaserFiredThisTurn() { return laserFiredThisTurn; }
    public void setLaserFiredThisTurn(boolean laserFiredThisTurn) { this.laserFiredThisTurn = laserFiredThisTurn; }

    public Rectangle getVisionBounds() { return visionBounds; }
    public Rectangle getLaserBounds() { return laserBounds; }

    public float getLaserLength() { return laserLength; }
    public void setLaserLength(float laserLength) { this.laserLength = laserLength; }
}
