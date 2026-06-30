package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.math.Rectangle;

public class ZoteModel {
    private float x, y;
    private Rectangle bounds;
    private boolean facingRight = false;

    private ZoteState currentState = ZoteState.IDLE;
    private float stateTimer = 0f;
    private int hitCount = 0;
    private boolean isEnraged = false;
    private float angerTimer = 0f;
    public static final float ANGER_DURATION = 15f;
    public static final float ROLL_DISTANCE_THRESHOLD = 250f;


    // دیالوگ‌ها
    private boolean playerInRange = false;
    private boolean isTalking = false;
    private int dialogueIndex = 0;
    private boolean mainDialogueFinished = false;
    private String currentDisplayLine = "";

    public String[] mainDialogues = {
        "I am Zote the Mighty, a knight of great renown!",
        "How dare you interrupt me?!",
        "Step aside, I have important quests to fulfill."
    };

    public String[] precepts = {
        "Precept 1: Always Win Your Battles.",
        "Precept 2: Never Let Them Laugh at You.",
        "Precept 3: Always Be Rested."
    };

    public ZoteModel(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 70, 90);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public void takeDamage() {
        if (isEnraged || currentState != ZoteState.IDLE) return;
        hitCount++;
    }


    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public Rectangle getBounds() { return bounds; }
    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean f) { this.facingRight = f; }
    public ZoteState getCurrentState() { return currentState; }
    public void setCurrentState(ZoteState s) { this.currentState = s; }
    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float t) { this.stateTimer = t; }
    public boolean isPlayerInRange() { return playerInRange; }
    public void setPlayerInRange(boolean p) { this.playerInRange = p; }
    public boolean isTalking() { return isTalking; }
    public void setTalking(boolean t) { this.isTalking = t; }
    public int getDialogueIndex() { return dialogueIndex; }
    public void setDialogueIndex(int d) { this.dialogueIndex = d; }
    public boolean isMainDialogueFinished() { return mainDialogueFinished; }
    public void setMainDialogueFinished(boolean m) { this.mainDialogueFinished = m; }
    public String getCurrentDisplayLine() { return currentDisplayLine; }
    public void setCurrentDisplayLine(String s) { this.currentDisplayLine = s; }
    public int getHitCount() { return hitCount; }
    public void setHitCount(int h) { this.hitCount = h; }
    public boolean isEnraged() { return isEnraged; }
    public void setEnraged(boolean e) { this.isEnraged = e; }
    public float getAngerTimer() { return angerTimer; }
    public void setAngerTimer(float t) { this.angerTimer = t; }
}
