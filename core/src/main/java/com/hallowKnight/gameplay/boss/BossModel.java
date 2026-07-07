package com.hallowKnight.gameplay.boss;

import com.badlogic.gdx.math.Rectangle;

public class BossModel {

    public static final int MAX_HP          = 10;
    public static final int STUN_HP_TRIGGER = MAX_HP / 2;

    public static final float PHASE2_SPEED_MULT = 1.4f;
    public static final float CHARGE_SPEED       = 520f;
    public static final float JUMP_VEL_Y         = 900f;
    public static final float JUMP_VEL_X         = 350f;

    public static final float MACE_SLAM_WINDUP      = 0.5f;
    public static final float MACE_SLAM_ACTIVE       = 0.25f;
    public static final float MACE_SLAM_RECOVER      = 0.4f;
    public static final float CHARGE_ANTICIPATION    = 0.2f;
    public static final float JUMP_ANTIC_DURATION    = 0.3f;
    public static final float LAND_DURATION          = 0.4f;
    public static final float TURN_DURATION          = 0.2f;
    public static final float STUN_DURATION          = 10.0f;
    public static final float STUN_RECOVER_DURATION  = 0.7f;
    public static final float IDLE_DURATION          = 0.3f;

    public static final float CLOSE_RANGE    = 180f;
    public static final float MID_RANGE      = 420f;

    private BossState lastMove = null;
    private BossState secondLastMove = null;

    private float x, y;
    private float velocityX = 0, velocityY = 0;
    private boolean facingRight = true;
    private boolean isGrounded = false;

    private final Rectangle bounds;

    private int hp = MAX_HP;
    private boolean isDead = false;

    private float invulnerabilityTimer = 0f;
    private float actionCooldown = 1.0f;

    private int recentHitCount = 0;
    private float hitTimer = 0f;

    private float hitReactTimer = 0f;
    private float hitReactStateTime = 0f;

    private BossState currentState = BossState.IDLE;
    private float stateTimer = 0f;
    private boolean phase2 = false;
    private boolean stunTriggered = false;

    private boolean deathStarted = false;

    private boolean attackDamageDealt = false;
    private Rectangle attackHitbox;

    private float cameraShakeTimer = 0f;
    private float cameraShakeIntensity = 0f;

    private boolean spellHit = false;
    private boolean wraithHit = false;

    private java.util.List<ShockwaveModel> shockwaves;

    public BossModel(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 250, 300);
        this.attackHitbox = new Rectangle();
        this.shockwaves = new java.util.ArrayList<>();
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public void recordMove(BossState move) {
        secondLastMove = lastMove;
        lastMove = move;
    }

    public boolean isSpam(BossState move) {
        return move == lastMove && move == secondLastMove;
    }

    public BossState getLastMove() { return lastMove; }

    public void takeDamage(int amount) {
        if (invulnerabilityTimer > 0) return;
        invulnerabilityTimer = 0.5f;

        hitReactTimer = 0.3f;
        hitReactStateTime = 0f;

        if (isDead) return;

        hp -= amount;
        recentHitCount++;
        hitTimer = 4.0f;

        if (hp <= 0) {
            hp = 0;
            isDead = true;
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; updateBounds(); }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; updateBounds(); }
    public float getVelocityX() { return velocityX; }
    public void setVelocityX(float vx) { this.velocityX = vx; }
    public float getVelocityY() { return velocityY; }
    public void setVelocityY(float vy) { this.velocityY = vy; }
    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean f) { this.facingRight = f; }
    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean g) { this.isGrounded = g; }
    public Rectangle getBounds() { return bounds; }
    public int getHp() { return hp; }
    public boolean isDead() { return isDead; }
    public BossState getCurrentState() { return currentState; }
    public void setCurrentState(BossState s) { this.currentState = s; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float t) { this.stateTimer = t; }
    public boolean isPhase2() { return phase2; }
    public void setPhase2(boolean p) { this.phase2 = p; }
    public boolean isStunTriggered() { return stunTriggered; }
    public void setStunTriggered(boolean s) { this.stunTriggered = s; }
    public boolean isDeathStarted() { return deathStarted; }
    public void setDeathStarted(boolean d) { this.deathStarted = d; }
    public boolean isAttackDamageDealt() { return attackDamageDealt; }
    public void setAttackDamageDealt(boolean a) { this.attackDamageDealt = a; }
    public Rectangle getAttackHitbox() { return attackHitbox; }
    public float getCameraShakeTimer() { return cameraShakeTimer; }
    public void setCameraShakeTimer(float t) { this.cameraShakeTimer = t; }
    public float getCameraShakeIntensity() { return cameraShakeIntensity; }
    public void setCameraShakeIntensity(float i) { this.cameraShakeIntensity = i; }
    public boolean isSpellHit() { return spellHit; }
    public void setSpellHit(boolean s) { this.spellHit = s; }
    public boolean isWraithHit() { return wraithHit; }
    public void setWraithHit(boolean w) { this.wraithHit = w; }
    public float getInvulnerabilityTimer() { return invulnerabilityTimer; }
    public void setInvulnerabilityTimer(float invulnerabilityTimer) { this.invulnerabilityTimer = invulnerabilityTimer; }
    public float getActionCooldown() { return actionCooldown; }
    public void setActionCooldown(float actionCooldown) { this.actionCooldown = actionCooldown; }
    public java.util.List<ShockwaveModel> getShockwaves() { return shockwaves; }

    public int getRecentHitCount() { return recentHitCount; }
    public void setRecentHitCount(int recentHitCount) { this.recentHitCount = recentHitCount; }
    public float getHitTimer() { return hitTimer; }
    public void setHitTimer(float hitTimer) { this.hitTimer = hitTimer; }

    public float getHitReactTimer() { return hitReactTimer; }
    public void setHitReactTimer(float hitReactTimer) { this.hitReactTimer = hitReactTimer; }
    public float getHitReactStateTime() { return hitReactStateTime; }
    public void setHitReactStateTime(float hitReactStateTime) { this.hitReactStateTime = hitReactStateTime; }

    public void triggerCameraShake(float intensity, float duration) {
        this.cameraShakeIntensity = intensity;
        this.cameraShakeTimer = duration;
    }
}
