package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.math.Rectangle;

public class ZoteModel {
    private float x, y;
    private Rectangle bounds;
    private boolean facingRight = false;

    public enum State { IDLE, TALK, TURN, ATTACK, FALL, GET_UP, ROLL }
    private State currentState = State.IDLE;
    private float stateTimer = 0f;

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

    private int hitCount = 0;

    public ZoteModel(float x, float y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, 60, 80);
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
    }

    public void takeDamage() { hitCount++; }

    // Getters and Setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public Rectangle getBounds() { return bounds; }
    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean f) { this.facingRight = f; }
    public State getCurrentState() { return currentState; }
    public void setCurrentState(State s) { this.currentState = s; }
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
}
