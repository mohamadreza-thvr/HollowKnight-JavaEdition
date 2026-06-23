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
        if (delta > 0.05f) {
            delta = 0.05f;
        }
        if (model.isDead()) {
            model.setDeathTimer(model.getDeathTimer() + delta);
            model.setVelocityX(0);
            applyPhysics(delta, mapModel);

            if (model.getDeathTimer() > 1.4f) {
                game.setScreen(new GameScreen(game));
            }
            return;
        }
        if (model.isAttacking()) {
            model.setAttackTimer(model.getAttackTimer() + delta);
            if (model.getAttackTimer() >= 0.4f) {
                model.setAttacking(false);
            }
        }

        model.setWasGrounded(model.isGrounded());

        handleInput(delta);
        applyPhysics(delta, mapModel);

        if (model.isGrounded() || model.isWallSliding()) {
            model.setCanDoubleJump(true);
            model.setCanDash(true);
            model.setDoubleJumping(false);
        }

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

        if (!model.wasGrounded() && model.isGrounded()) {
            model.setLanding(true);
            model.setLandingTimer(0.4f);
        }

        if (model.isLanding()) {
            model.setLandingTimer(model.getLandingTimer() - delta);
            if (model.getLandingTimer() <= 0) {
                model.setLanding(false);
            }
        }
    }

    private void handleInput(float delta) {
        if (model.isDashing()) {
            model.setDashTimer(model.getDashTimer() - delta);
            model.setVelocityY(0);

            if (model.getDashTimer() <= 0) {
                model.setDashing(false);
                model.setVelocityX(0);
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C) && model.isCanDash()) {
            model.setDashing(true);
            model.setCanDash(false);
            model.setDashTimer(0.3f);

            float dashSpeed = Constance.KNIGHT_SPEED * 2.5f;
            model.setVelocityX(model.isFacingRight() ? dashSpeed : -dashSpeed);
            model.setVelocityY(0);

            // قطع کردن حالت‌های دیگر
            model.setFocusing(false);
            model.setWallSliding(false);
            return;
        }


        model.setVelocityX(0);
        model.setLookingUp(false);
        model.setLookingDown(false);

        if (model.getWallJumpTimer() > 0) {
            model.setWallJumpTimer(model.getWallJumpTimer() - delta);
        }

        if (!model.isDashing() && !model.isFocusing()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !model.isAttacking()) {
                model.setAttacking(true);
                model.setAttackTimer(0f);

                // تشخیص جهت حمله
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    model.setAttackDirection(1); // ضربه رو به بالا
                }

                else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !model.isGrounded()) {
                    model.setAttackDirection(-1);
                }
                else {
                    model.setAttackDirection(0);
                    model.setAltSlash(!model.isAltSlash());
                }
            }
        }

        if (model.getWallJumpTimer() <= 0) {
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
        }

        if (model.isGrounded() && model.getVelocityX() == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (!model.isLanding()) model.setLookingUp(true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                model.setLookingDown(true);
                model.setLanding(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (model.isGrounded()) {
                model.setVelocityY(Constance.JUMP_FORCE);
                model.setGrounded(false);
                model.setLanding(false);
            } else if (model.isWallSliding()) {
                model.setVelocityY(Constance.JUMP_FORCE * 0.9f);
                model.setVelocityX(-model.getWallDirection() * Constance.KNIGHT_SPEED * 1.5f);
                model.setWallSliding(false);
                model.setWallJumpTimer(0.25f);
                model.setFacingRight(model.getWallDirection() == 1);
            } else if (model.isCanDoubleJump()) {
                model.setVelocityY(Constance.JUMP_FORCE * 0.9f);
                model.setCanDoubleJump(false);
                model.setDoubleJumping(true);
                model.setWallSliding(false);
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (model.getVelocityY() > 0 && model.getWallJumpTimer() <= 0) {
                model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * 2.5f * delta));
            }
        }

        // تمرکز
        if (Gdx.input.isKeyPressed(Input.Keys.A) && model.isGrounded()) {
            model.setVelocityX(0);
            model.setFocusing(true);
            if (model.getCurrentHealth() < 5 && model.getCurrentSoul() >= 20) {
                model.setFocusTimer(model.getFocusTimer() + delta);
                if (model.getFocusTimer() >= 1.0f) {
                    model.setCurrentSoul(model.getCurrentSoul() - 20);
                    model.heal(1);
                    model.setFocusTimer(0f);
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
        boolean touchingWall = false;

        model.setX(model.getX() + (model.getVelocityX() * delta));
        model.updateBounds();

        if (checkHazards(mapModel)) return;

        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (model.getBounds().overlaps(solid)) {

                Rectangle lowerHalf = new Rectangle(
                    model.getBounds().x,
                    model.getBounds().y,
                    model.getBounds().width,
                    model.getBounds().height * 0.5f
                );

                boolean touchesLowerHalf = lowerHalf.overlaps(solid);

                if (model.getVelocityX() > 0) {
                    model.setX(solid.x - model.getBounds().width);
                    if (touchesLowerHalf) {
                        touchingWall = true;
                        model.setWallDirection(1);
                    }
                }
                else if (model.getVelocityX() < 0) {
                    model.setX(solid.x + solid.width);
                    if (touchesLowerHalf) {
                        touchingWall = true;
                        model.setWallDirection(-1);
                    }
                }
                model.setVelocityX(0);
                if (model.isDashing()) { model.setDashing(false); }
                model.updateBounds();
            }
        }

        if (touchingWall && !model.isGrounded() && model.getVelocityY() < 0) {
            if ((model.getWallDirection() == 1 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
                (model.getWallDirection() == -1 && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                model.setWallSliding(true);
            } else {
                model.setWallSliding(false);
            }
        } else {
            model.setWallSliding(false);
        }

        // اعمال جاذبه
        if (!model.isDashing()) {
            if (model.isWallSliding()) {
                float maxSlideSpeed = -Constance.KNIGHT_SPEED * 0.4f;
                model.setVelocityY(Math.max(model.getVelocityY() - (Constance.GRAVITY * delta), maxSlideSpeed));
            } else {
                model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * delta));
            }
        }

        model.setY(model.getY() + (model.getVelocityY() * delta));
        model.updateBounds();

        if (checkHazards(mapModel)) return;

        model.setGrounded(false);

        for (com.badlogic.gdx.math.Rectangle solid : mapModel.getSolidBounds()) {
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

    private boolean checkHazards(com.hallowKnight.gameplay.map.MapModel mapModel) {
        for (Rectangle spike : mapModel.getDeadlyBounds()) {
            if (model.getBounds().overlaps(spike)) {

                model.takeDamage(1);

                if (!model.isDead()) {
                    model.setX(model.getLastSafeX());
                    model.setY(model.getLastSafeY() + 10);
                    model.setVelocityX(0);
                    model.setVelocityY(0);
                    model.updateBounds();
                }
                return true;
            }
        }
        return false;
    }
}
