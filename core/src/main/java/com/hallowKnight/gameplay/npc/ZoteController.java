package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;

public class ZoteController {

    private static final float ROLL_SPEED = 500f;
    private static final float ATTACK_SPEED = 250f;
    private MapModel mapModel;

    private Sound[] zoteVoices;

    public ZoteController() {
        zoteVoices = new Sound[3];
        zoteVoices[0] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/zote_talk_1.wav"));
        zoteVoices[1] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/zote_talk_2.wav"));
        zoteVoices[2] = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/zote_talk_3.wav"));
    }

    public void update(ZoteModel zote, float delta, KnightModel knight) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }

        zote.setStateTimer(zote.getStateTimer() + delta);

        float dist = Vector2.dst(knight.getX(), knight.getY(), zote.getX(), zote.getY());
        zote.setPlayerInRange(dist < 150f);

        if (!zote.isEnraged()) {
            if (zote.isPlayerInRange() && !zote.isTalking() && zote.getCurrentState() == ZoteState.IDLE) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    zote.setTalking(true);
                    zote.setCurrentState(ZoteState.TALK);
                    zote.setDialogueIndex(0);
                    zote.setFacingRight(knight.getX() > zote.getX());
                    updateDialogueText(zote);
                }
            }

            if (zote.isTalking() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                zote.setDialogueIndex(zote.getDialogueIndex() + 1);
                if (!zote.isMainDialogueFinished()) {
                    if (zote.getDialogueIndex() >= zote.mainDialogues.length) {
                        endDialogue(zote);
                        zote.setMainDialogueFinished(true);
                    } else {
                        updateDialogueText(zote);
                    }
                } else {
                    endDialogue(zote);
                }
            }
        }

        if (zote.getHitCount() >= 3 && !zote.isEnraged() && zote.getCurrentState() == ZoteState.IDLE) {
            zote.setEnraged(true);
            zote.setAngerTimer(0f);
            zote.setHitCount(0);
            zote.setTalking(false);
            startAttack(zote, knight);
        }

        if (zote.isEnraged()) {
            zote.setAngerTimer(zote.getAngerTimer() + delta);

            if (zote.getAngerTimer() >= ZoteModel.ANGER_DURATION) {
                zote.setEnraged(false);
                zote.setCurrentState(ZoteState.IDLE);
                zote.setStateTimer(0f);
                zote.setHitCount(0);
                return;
            }
        }

        if (zote.getCurrentState() == ZoteState.ATTACK) {
            zote.setX(zote.getX() + (zote.isFacingRight() ? ATTACK_SPEED * delta : -ATTACK_SPEED * delta));
            zote.updateBounds();

            if (zote.getBounds().overlaps(knight.getBounds()) || zote.getStateTimer() > 1.5f) {
                zote.setCurrentState(ZoteState.FALL);
                zote.setStateTimer(0f);
                zote.setX(zote.getX() + (zote.isFacingRight() ? -30 : 30));
                zote.updateBounds();
            }
        }
        else if (zote.getCurrentState() == ZoteState.ROLL) {
            zote.setX(zote.getX() + (zote.isFacingRight() ? ROLL_SPEED * delta : -ROLL_SPEED * delta));
            zote.updateBounds();

            if (zote.getBounds().overlaps(knight.getBounds())) {
                zote.setCurrentState(ZoteState.FALL);
                zote.setStateTimer(0f);
                zote.setX(zote.getX() + (zote.isFacingRight() ? -30 : 30));
                zote.updateBounds();
            } else if (zote.getStateTimer() > 0.3f) {
                decideNextAction(zote, knight);
            }
        }
        else if (zote.getCurrentState() == ZoteState.FALL) {
            if (zote.getStateTimer() > 1.0f) {
                zote.setCurrentState(ZoteState.GET_UP);
                zote.setStateTimer(0f);
            }
        }
        else if (zote.getCurrentState() == ZoteState.GET_UP) {
            if (zote.getStateTimer() > 0.5f) {
                if (zote.isEnraged()) {
                    decideNextAction(zote, knight);
                } else {
                    zote.setCurrentState(ZoteState.IDLE);
                    zote.setStateTimer(0f);
                }
            }
        }
        else if (zote.getCurrentState() == ZoteState.TURN) {
            if (zote.getStateTimer() > 0.2f) {
                decideNextAction(zote, knight);
            }
        }
    }

    private void decideNextAction(ZoteModel zote, KnightModel knight) {
        boolean knightIsRight = knight.getX() > zote.getX();

        if (knightIsRight != zote.isFacingRight() && zote.getCurrentState() != ZoteState.TURN) {
            zote.setCurrentState(ZoteState.TURN);
            zote.setStateTimer(0f);
            return;
        }

        zote.setFacingRight(knightIsRight);
        startAttack(zote, knight);
    }

    private void startAttack(ZoteModel zote, KnightModel knight) {
        float dist = Vector2.dst(knight.getX(), knight.getY(), zote.getX(), zote.getY());
        zote.setFacingRight(knight.getX() > zote.getX());
        zote.setStateTimer(0f);

        if (dist > ZoteModel.ROLL_DISTANCE_THRESHOLD) {
            zote.setCurrentState(ZoteState.ROLL);
        } else {
            zote.setCurrentState(ZoteState.ATTACK);
        }
    }

    private void updateDialogueText(ZoteModel zote) {
        if (!zote.isMainDialogueFinished()) {
            zote.setCurrentDisplayLine(zote.mainDialogues[zote.getDialogueIndex()]);
        } else {
            int randomPrecept = (int)(Math.random() * zote.precepts.length);
            zote.setCurrentDisplayLine(zote.precepts[randomPrecept]);
        }

        playRandomZoteVoice();
    }

    private void playRandomZoteVoice() {
        if (zoteVoices != null && zoteVoices.length > 0) {
            int randomIndex = (int) (Math.random() * zoteVoices.length);
            zoteVoices[randomIndex].play(1.0f);
        }
    }

    private void endDialogue(ZoteModel zote) {
        zote.setTalking(false);
        zote.setCurrentState(ZoteState.IDLE);
        zote.setStateTimer(0f);
    }

    public void dispose() {
        if (zoteVoices != null) {
            for (Sound sound : zoteVoices) {
                if (sound != null) sound.dispose();
            }
        }
    }
}
