package com.hallowKnight.gameplay.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.gameplay.knight.KnightModel;

public class ZoteController {

    public void update(ZoteModel zote, float delta, KnightModel knight) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }

        zote.setStateTimer(zote.getStateTimer() + delta);

        float dist = Vector2.dst(knight.getX(), knight.getY(), zote.getX(), zote.getY());
        zote.setPlayerInRange(dist < 150f);

        if (zote.isPlayerInRange() && !zote.isTalking() && zote.getCurrentState() == ZoteModel.State.IDLE) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                zote.setTalking(true);
                zote.setCurrentState(ZoteModel.State.TALK);
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

        if (zote.getHitCount() >= 3 && zote.getCurrentState() == ZoteModel.State.IDLE) {
            zote.setCurrentState(ZoteModel.State.ATTACK);
            zote.setStateTimer(0f);
            zote.setHitCount(0);
            zote.setFacingRight(knight.getX() > zote.getX());
        }

        if (zote.getCurrentState() == ZoteModel.State.ATTACK) {
            float speed = 250f;
            zote.setX(zote.getX() + (zote.isFacingRight() ? speed * delta : -speed * delta));
            zote.updateBounds();

            if (zote.getBounds().overlaps(knight.getBounds()) || zote.getStateTimer() > 1.5f) {
                zote.setCurrentState(ZoteModel.State.FALL);
                zote.setStateTimer(0f);
                zote.setX(zote.getX() + (zote.isFacingRight() ? -30 : 30));
                zote.updateBounds();
            }
        }
        else if (zote.getCurrentState() == ZoteModel.State.FALL) {
            if (zote.getStateTimer() > 1.0f) {
                zote.setCurrentState(ZoteModel.State.GET_UP);
                zote.setStateTimer(0f);
            }
        }
        else if (zote.getCurrentState() == ZoteModel.State.GET_UP) {
            if (zote.getStateTimer() > 0.5f) {
                zote.setCurrentState(ZoteModel.State.IDLE);
            }
        }
    }

    private void updateDialogueText(ZoteModel zote) {
        if (!zote.isMainDialogueFinished()) {
            zote.setCurrentDisplayLine(zote.mainDialogues[zote.getDialogueIndex()]);
        } else {
            int randomPrecept = (int)(Math.random() * zote.precepts.length);
            zote.setCurrentDisplayLine(zote.precepts[randomPrecept]);
        }
    }

    private void endDialogue(ZoteModel zote) {
        zote.setTalking(false);
        zote.setCurrentState(ZoteModel.State.IDLE);
    }
}
