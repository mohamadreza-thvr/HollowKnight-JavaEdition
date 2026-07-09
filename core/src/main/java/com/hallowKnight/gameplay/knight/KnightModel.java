package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;
import java.util.ArrayList;
import java.util.List;

public class KnightModel {

    private boolean isGodMode = false;
    private boolean isNoclip = false;
    private boolean requestBossTeleport = false;

    private int totalKills = 0;
    private int totalDeaths = 0;
    private float totalTime = 0f;

    public enum Charm {
        SOUL_CATCHER, DASHMASTER, UNBREAKABLE_STRENGTH,
        QUICK_SLASH, QUICK_FOCUS, HEAVY_BLOW,
        SHARP_SHADOW, VOID_HEART
    }

    private int maxHealth = 10;
    private int currentHealth = 10;
    private boolean isInvulnerable = false;
    private float invulnerabilityTimer = 0f;

    private float maxSoul = 100f;
    private float currentSoul = 100f;

    private boolean isFocusing = false;
    private float focusTimer = 0f;

    private float lastSafeX, lastSafeY;

    private float x, y;
    private float velocityX = 0, velocityY = 0;

    private boolean isGrounded = false;
    private boolean isFacingRight = true;
    private Rectangle bounds;

    private boolean lookingUp = false;
    private boolean lookingDown = false;
    private boolean isLanding = false;
    private float landingTimer = 0f;
    private boolean wasGrounded = false;

    private boolean isJumping = false;
    private boolean canDoubleJump = true;
    private boolean isDoubleJumping = false;

    private boolean isWallSliding = false;
    private int wallDirection = 0;
    private float wallJumpTimer = 0f;

    private boolean isDashing = false;
    private float dashTimer = 0f;
    private boolean canDash = true;
    private float dashCooldownTimer = 0f;

    private boolean isDead = false;
    private float deathTimer = 0f;

    private boolean isAttacking = false;
    private float attackTimer = 0f;
    private int attackDirection = 0;
    private boolean isAltSlash = false;

    private boolean isCasting = false;
    private boolean castingUp = false;
    private float castTimer = 0f;

    private List<Charm> equippedCharms = new ArrayList<>();
    private int maxNotches = 3;

    public KnightModel(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.bounds = new Rectangle(x, y, Constance.KNIGHT_WIDTH, Constance.KNIGHT_HEIGHT);
    }

    public void updateBounds() { bounds.setPosition(x, y); }

    public boolean isCharmEquipped(Charm charm) { return equippedCharms.contains(charm); }
    public boolean equipCharm(Charm charm) {
        if (equippedCharms.size() < maxNotches && !equippedCharms.contains(charm)) {
            equippedCharms.add(charm);
            return true;
        }
        return false;
    }
    public void unequipCharm(Charm charm) { equippedCharms.remove(charm); }
    public List<Charm> getEquippedCharms() { return equippedCharms; }

    public float getDashCooldownTimer() { return dashCooldownTimer; }
    public void setDashCooldownTimer(float dashCooldownTimer) { this.dashCooldownTimer = dashCooldownTimer; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; updateBounds(); }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; updateBounds(); }
    public float getVelocityX() { return velocityX; }
    public void setVelocityX(float velocityX) { this.velocityX = velocityX; }
    public float getVelocityY() { return velocityY; }
    public void setVelocityY(float velocityY) { this.velocityY = velocityY; }
    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean grounded) { this.isGrounded = grounded; }
    public boolean isFacingRight() { return isFacingRight; }
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }
    public Rectangle getBounds() { return bounds; }
    public boolean isLookingUp() { return lookingUp; }
    public void setLookingUp(boolean lookingUp) { this.lookingUp = lookingUp; }
    public boolean isLookingDown() { return lookingDown; }
    public void setLookingDown(boolean lookingDown) { this.lookingDown = lookingDown; }
    public boolean isLanding() { return isLanding; }
    public void setLanding(boolean landing) { this.isLanding = landing; }
    public float getLandingTimer() { return landingTimer; }
    public void setLandingTimer(float landingTimer) { this.landingTimer = landingTimer; }
    public boolean wasGrounded() { return wasGrounded; }
    public void setWasGrounded(boolean wasGrounded) { this.wasGrounded = wasGrounded; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int health) { this.currentHealth = health; }
    public boolean isInvulnerable() { return isInvulnerable; }
    public void setInvulnerable(boolean invulnerable) { this.isInvulnerable = invulnerable; }
    public float getInvulnerabilityTimer() { return invulnerabilityTimer; }
    public void setInvulnerabilityTimer(float timer) { this.invulnerabilityTimer = timer; }
    public float getLastSafeX() { return lastSafeX; }
    public float getLastSafeY() { return lastSafeY; }
    public void setSafePosition(float x, float y) { this.lastSafeX = x; this.lastSafeY = y; }
    public float getCurrentSoul() { return currentSoul; }
    public void setCurrentSoul(float soul) { this.currentSoul = Math.max(0, Math.min(soul, maxSoul)); }
    public float getMaxSoul() { return maxSoul; }
    public boolean isFocusing() { return isFocusing; }
    public void setFocusing(boolean focusing) { this.isFocusing = focusing; }
    public float getFocusTimer() { return focusTimer; }
    public void setFocusTimer(float focusTimer) { this.focusTimer = focusTimer; }
    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { this.isJumping = jumping; }
    public boolean isCanDoubleJump() { return canDoubleJump; }
    public void setCanDoubleJump(boolean canDoubleJump) { this.canDoubleJump = canDoubleJump; }
    public boolean isDoubleJumping() { return isDoubleJumping; }
    public void setDoubleJumping(boolean doubleJumping) { this.isDoubleJumping = doubleJumping; }
    public boolean isWallSliding() { return isWallSliding; }
    public void setWallSliding(boolean wallSliding) { this.isWallSliding = wallSliding; }
    public int getWallDirection() { return wallDirection; }
    public void setWallDirection(int wallDirection) { this.wallDirection = wallDirection; }
    public float getWallJumpTimer() { return wallJumpTimer; }
    public void setWallJumpTimer(float wallJumpTimer) { this.wallJumpTimer = wallJumpTimer; }

    public boolean isDashing() { return isDashing; }
    public void setDashing(boolean dashing) { this.isDashing = dashing; }
    public float getDashTimer() { return dashTimer; }
    public void setDashTimer(float dashTimer) { this.dashTimer = dashTimer; }
    public boolean isCanDash() { return canDash; }
    public void setCanDash(boolean canDash) { this.canDash = canDash; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { this.isDead = dead; }
    public float getDeathTimer() { return deathTimer; }
    public void setDeathTimer(float deathTimer) { this.deathTimer = deathTimer; }

    public boolean isAttacking() { return isAttacking; }
    public void setAttacking(boolean attacking) { this.isAttacking = attacking; }

    public float getAttackTimer() { return attackTimer; }
    public void setAttackTimer(float attackTimer) { this.attackTimer = attackTimer; }

    public int getAttackDirection() { return attackDirection; }
    public void setAttackDirection(int attackDirection) { this.attackDirection = attackDirection; }

    public boolean isAltSlash() { return isAltSlash; }
    public void setAltSlash(boolean altSlash) { this.isAltSlash = altSlash; }

    public void heal(int amount) {
        if (currentHealth < maxHealth && !isDead) { currentHealth += amount; }
    }

    public boolean isCasting() { return isCasting; }
    public void setCasting(boolean casting) { this.isCasting = casting; }
    public float getCastTimer() { return castTimer; }
    public void setCastTimer(float castTimer) { this.castTimer = castTimer; }
    public boolean isCastingUp() { return castingUp; }
    public void setCastingUp(boolean c) { this.castingUp = c; }

    public int getTotalKills() { return totalKills; }
    public void setTotalKills(int totalKills) { this.totalKills = totalKills; }

    public int getTotalDeaths() { return totalDeaths; }
    public void setTotalDeaths(int totalDeaths) { this.totalDeaths = totalDeaths; }

    public float getTotalTime() { return totalTime; }
    public void setTotalTime(float totalTime) { this.totalTime = totalTime; }

    public boolean isGodMode() { return isGodMode; }
    public void setGodMode(boolean godMode) { this.isGodMode = godMode; }
    public boolean isNoclip() { return isNoclip; }
    public void setNoclip(boolean noclip) { this.isNoclip = noclip; }
    public boolean isRequestBossTeleport() { return requestBossTeleport; }
    public void setRequestBossTeleport(boolean request) { this.requestBossTeleport = request; }


    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public void takeDamage(int amount) {
        if (isGodMode) return;

        if (!isInvulnerable && !isDead) {
            currentHealth -= amount;
            if (currentHealth <= 0) {
                currentHealth = 0;
                isDead = true;
            } else {
                isInvulnerable = true;
                invulnerabilityTimer = 1.5f;
            }
        }
    }
}
