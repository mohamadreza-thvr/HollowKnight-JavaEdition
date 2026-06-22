package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.core.GameScreen;
import com.hallowKnight.gameplay.map.MapModel;

public class KnightController {
    private Game game;
    private KnightModel model;
    private MapModel mapModel;

    public KnightController(KnightModel model, MapModel mapModel, Game game) {
        this.model = model;
        this.mapModel = mapModel;
        this.game = game;
    }

    public void update(float delta) {
        model.setWasGrounded(model.isGrounded());

        handleInput(delta);
        applyPhysics(delta, mapModel);

        if (model.isInvulnerable()) {
            model.setInvulnerabilityTimer(model.getInvulnerabilityTimer() - delta);
            if (model.getInvulnerabilityTimer() <= 0) {
                model.setInvulnerable(false);
            }
        }

        if (model.isGrounded() && model.getVelocityY() == 0) {
            boolean nearHazard = false;
            for (com.badlogic.gdx.math.Rectangle spike : mapModel.getDeadlyBounds()) {
                if (model.getBounds().overlaps(spike)) {
                    nearHazard = true;
                    break;
                }
            }
            if (!nearHazard) {
                model.setSafePosition(model.getX(), model.getY());
            }
        }

        checkHazards(mapModel);

        if (!model.wasGrounded() && model.isGrounded()) {
            model.setLanding(true);
            model.setLandingTimer(0.2f);
        }

        if (model.isLanding()) {
            model.setLandingTimer(model.getLandingTimer() - delta);
            if (model.getLandingTimer() <= 0) {
                model.setLanding(false);
            }
        }
    }

    private void handleInput(float delta) {
        model.setVelocityX(0);
        model.setLookingUp(false);
        model.setLookingDown(false);

        // حرکت به چپ و راست
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            model.setVelocityX(-Constance.KNIGHT_SPEED);
            model.setFacingRight(false);
            model.setLanding(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            model.setVelocityX(Constance.KNIGHT_SPEED);
            model.setFacingRight(true);
            model.setLanding(false);
        }

        // نگاه کردن به بالا و پایین
        if (model.isGrounded() && model.getVelocityX() == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if(!model.isLanding()){
                    model.setLookingUp(true);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                model.setLookingDown(true);
                model.setLanding(false);
            }
        }

        // پرش
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && model.isGrounded()) {
            model.setVelocityY(Constance.JUMP_FORCE);
            model.setGrounded(false);
            model.setLanding(false);
            model.setJumping(true);
            model.setJumpTimer(0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z) && model.isJumping()) {
            model.setJumpTimer(model.getJumpTimer() + delta);

            if (model.getJumpTimer() < 0.2f) {
                model.setVelocityY(Constance.JUMP_FORCE);
            } else {
                model.setJumping(false);
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.Z)) {
            model.setJumping(false);

            if (model.getVelocityY() > 0) {
                model.setVelocityY(model.getVelocityY() * 0.5f);
            }
        }

        // سیستم تمرکز (Focus)
        if (Gdx.input.isKeyPressed(Input.Keys.C) && model.isGrounded()) {
            model.setVelocityX(0); // هنگام تمرکز نباید حرکت کند
            model.setFocusing(true);

            if (model.getCurrentHealth() < 5 && model.getCurrentSoul() >= 20) {
                model.setFocusTimer(model.getFocusTimer() + delta);

                if (model.getFocusTimer() >= 1.0f) {
                    model.setCurrentSoul(model.getCurrentSoul() - 20);
                    model.heal(1);
                    model.setFocusTimer(0f);
                    System.out.println("Healed! Current Health: " + model.getCurrentHealth());
                }
            } else {
                model.setFocusTimer(0f);
            }
        } else {
            model.setFocusing(false);
            model.setFocusTimer(0f);
        }
    }

    private void applyPhysics(float delta, MapModel mapModel) {
        model.setX(model.getX() + (model.getVelocityX() * delta));
        model.updateBounds();

        for (com.badlogic.gdx.math.Rectangle solid : mapModel.getSolidBounds()) {
            if (model.getBounds().overlaps(solid)) {

                if (model.getVelocityX() > 0) {
                    model.setX(solid.x - model.getBounds().width);
                }
                else if (model.getVelocityX() < 0) {
                    model.setX(solid.x + solid.width);
                }
                model.setVelocityX(0);
                model.updateBounds();
            }
        }

        model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * delta));
        model.setY(model.getY() + (model.getVelocityY() * delta));
        model.updateBounds();

        model.setGrounded(false);

        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (model.getBounds().overlaps(solid)) {

                if (model.getVelocityY() < 0) {
                    model.setY(solid.y + solid.height);
                    model.setVelocityY(0);
                    model.setGrounded(true);
                }
                else if (model.getVelocityY() > 0) {
                    model.setY(solid.y - model.getBounds().height);
                    model.setVelocityY(0);
                }
                model.updateBounds();
            }
        }
    }

    private void checkHazards(com.hallowKnight.gameplay.map.MapModel mapModel) {
        for (Rectangle spike : mapModel.getDeadlyBounds()) {
            if (model.getBounds().overlaps(spike)) {
                if(model.getCurrentHealth() > 1){
                    model.takeDamage(1);
                    model.setX(model.getLastSafeX());
                    model.setY(model.getLastSafeY() + 10);
                    model.setVelocityX(0);
                    model.setVelocityY(0);
                } else {
                    game.setScreen(new GameScreen(game));
                    return;
                }

                model.updateBounds();
                break;
            }
        }
    }
}
