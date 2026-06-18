package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.map.MapModel;

public class KnightController {
    private KnightModel model;
    private MapModel mapModel;


    public KnightController(KnightModel model,  MapModel mapModel) {
        this.model = model;
        this.mapModel = mapModel;
    }

    public void update(float delta) {
        model.setWasGrounded(model.isGrounded());

        handleInput(delta);
        applyPhysics(delta, mapModel);

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
                model.setJumping(false); // زمان تمام شد
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.Z)) {
            model.setJumping(false);

            if (model.getVelocityY() > 0) {
                model.setVelocityY(model.getVelocityY() * 0.5f);
            }
        }
    }

    private void applyPhysics(float delta, MapModel mapModel) {
        model.setX(model.getX() + (model.getVelocityX() * delta));
        model.updateBounds(); // هیت باکس را آپدیت می‌کنیم

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
}
