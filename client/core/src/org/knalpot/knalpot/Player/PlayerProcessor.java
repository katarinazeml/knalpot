package org.knalpot.knalpot;

import java.lang.Math;
import java.lang.System;

import com.badlogic.gdx.Gdx;

public class PlayerProcessor {
	private World world;
	private Player player;

	private static final float SPEED = 120f;
    private static final float JUMP_HEIGHT = 305f;

    private static final float ACCELERATION = 13f;
    private static final float DECCELERATION = 16f;
    private static final float VELOCITY_POWER = 0.9f;

    private static float moveInput;

    // Implementing jump mechanics
    private boolean isGrounded = false;
    private float lastGroundedTime = 0f;
    private float lastJumpTime = 0f;

    // Gravity related
    private float gravityForce = Constants.GRAVITY_FORCE;

	public PlayerProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
	}

	public void update(float dt) {
		gravity();
		keyboardClick();
		move();
		temporaryCollision();

		if (!isGrounded) player.getAcceleration().y = -gravityForce;
		player.getAcceleration().scl(dt);
		player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);

		player.update(dt);
	}

	private void gravity() {
		if (player.getVelocity().y < 0) gravityForce = Constants.GRAVITY_FORCE * Constants.GRAVITY_ACCEL;
	}

	private void keyboardClick() {
		boolean isLeftPressed = Gdx.input.isKeyPressed(Constants.LEFT_KEY);
        boolean isRightPressed = Gdx.input.isKeyPressed(Constants.RIGHT_KEY);
        boolean isSpacePressed = Gdx.input.isKeyJustPressed(Constants.SPACEBAR);

		if (isLeftPressed) moveInput = -1;
        if (isRightPressed) moveInput = 1;
        if ((!isLeftPressed && !isRightPressed) || (isLeftPressed && isRightPressed)) moveInput = 0;
        if (isSpacePressed && isGrounded) {
        	jump();
        }
	}

	private void move() {
        // These variables are taken from YouTube tutorial on smooth platformer movements.
        float targetSpeed = moveInput * SPEED; // Direction of movement.
        float speedDifference = targetSpeed - player.getVelocity().x; // Difference between current desired velocity.
        float accelerationRate = (Math.abs(targetSpeed) > 0.01f) ? ACCELERATION : DECCELERATION;
        float movement = (float) Math.pow(Math.abs(speedDifference) * accelerationRate, VELOCITY_POWER) * Math.signum(speedDifference);

        player.getAcceleration().x = movement;
    }

    private void jump() {
    	lastGroundedTime = 0;
    	lastJumpTime = 0;
    	isGrounded = false;
    	player.getVelocity().y = JUMP_HEIGHT;
    }

    private void temporaryCollision() {
        if (player.getHitbox().x < 0) player.getAcceleration().x = 0;
        if (player.getHitbox().x > 800 - player.WIDTH) player.getAcceleration().x = 800 - player.WIDTH;
        if (player.getPosition().y + player.getHitbox().y < 0) {
        	isGrounded = true;
        	player.getPosition().y = 0f;
        	player.getVelocity().y = 0f;
        }
        if (player.getHitbox().y > 480 - player.HEIGHT) player.getVelocity().y = 0;
    }
}