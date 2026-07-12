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



    private boolean playerInRange = false;
    private boolean isTalking = false;
    private int dialogueIndex = 0;
    private boolean mainDialogueFinished = false;
    private String currentDisplayLine = "";

    public String[] mainDialogues = {
        "Hmm? What do you want, cur? Have you come to laugh at me as well?\n" +
            "Fools! Did you never consider that being trapped in this cage was precisely what I intended?!",
        "Yes, by all means, release me into your arena!",
        "I am Zote the Mighty! And as soon as I draw my nail, 'Life Ender', your laughter will turn into terror!"
    };

    public String[] precepts = {
        "Leave me be, you lowly worm! I must rest before my great battle. Pray we do not meet in the arena...",
        "\tHmm? Are you lost in these dark tunnels? I don't care.\n" +
            "No, I won't lead you out. I know the way, due to my powerful memory, but I only travel alone. Begone.",
        "\tSo, the kingdom was hiding this dark nest full of deadly predators. Well, now the deadliest predator of all is here...\n" +
            "...Zote the Mighty!"
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
