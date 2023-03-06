package org.knalpot.knalpot.Player;

import java.lang.Math;
import java.lang.System;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Addons.Constants;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.World.World;

public class PlayerProcessor {
	private World world;
	private Player player;
    private CollisionBlock collisionBlock;

	private final float SPEED = 120f;
    private final float JUMP_HEIGHT = 320f;

    private final float ACCELERATION = 12f;
    private final float DECCELERATION = 16f;
    private final float VELOCITY_POWER = 0.9f;

    private float moveInput;

    // Implementing jump mechanics
    private boolean canJump = true;
    private float lastGroundedTime = 0f;
    private float lastJumpTime = 0f;

    // Gravity related
    private float gravityForce = Constants.GRAVITY_FORCE;

	public PlayerProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
        collisionBlock = world.getCollisionBlocks();
	}

	public void update(float dt) {
		gravity();
        windowCollision();
        horizontalMovement();
        verticalMovement();
        //collide();

    	player.getAcceleration().scl(dt);
		player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);

		player.update(dt);
	}

	private void gravity() {
		if (player.getVelocity().y < 0) gravityForce = Constants.GRAVITY_FORCE * Constants.GRAVITY_ACCEL;
        player.getAcceleration().y = -gravityForce;
	}

    private void horizontalMovement() {
        boolean isLeftPressed = Gdx.input.isKeyPressed(Constants.LEFT_KEY);
        boolean isRightPressed = Gdx.input.isKeyPressed(Constants.RIGHT_KEY);
        if (isLeftPressed) {
            moveInput = -1;
            player.state = Player.State.MOVE;
            move();
        }
        if (isRightPressed) {
            moveInput = 1;
            player.state = Player.State.MOVE;
            move();
        }
        if ((!isLeftPressed && !isRightPressed) || (isLeftPressed && isRightPressed)) {
            moveInput = 0;
            player.state = Player.State.IDLE;
            move();
        }

    }

	private void verticalMovement() {
        boolean isSpacePressed = Gdx.input.isKeyJustPressed(Constants.SPACEBAR);

        if (isSpacePressed && canJump) {
        	jump();
        }
	}

	private void move() {
        // These variables are taken from YouTube tutorial on smooth platformer movements.
        float targetSpeed = moveInput * SPEED; // Direction of movement.
        //System.out.println("target speed");
        //System.out.println(targetSpeed);
        float speedDifference = targetSpeed - player.getVelocity().x; // Difference between current desired velocity.
        //System.out.println("speed difference");
        //System.out.println(speedDifference);
        float accelerationRate = (Math.abs(targetSpeed) > 0.01f) ? ACCELERATION : DECCELERATION;
        //System.out.println("accel rate");
        //System.out.println(accelerationRate);
        float movement = (float) Math.pow(Math.abs(speedDifference) * accelerationRate, VELOCITY_POWER) * Math.signum(speedDifference);
        //System.out.println("movement speed");
        //System.out.println((int) movement);

        player.getAcceleration().x = (int) movement;
    }

    private void jump() {
    	lastGroundedTime = 0;
    	lastJumpTime = 0;
        canJump = false;
        player.state = Player.State.JUMP;
    	player.getVelocity().y = JUMP_HEIGHT;
    }

    private void windowCollision() {
        if (player.getBounds().x < 200) {
            player.getPosition().x = 200f;
            player.getVelocity().x = 0f;
        }
        if (player.getBounds().x + player.getWidth() > 600) {
            player.getPosition().x = 600 - player.getWidth();
            player.getVelocity().x = 0f;
        }
        if (player.getPosition().y + player.getBounds().y < 0) {
            canJump = true;
            player.state = Player.State.IDLE;
        	player.getPosition().y = 0f;
            player.getVelocity().y = 0f;
        }
        if (player.getBounds().y > 480 - player.getHeight()) player.getVelocity().y = 0;
    }

    /*private void collide() {
        float colLeft = collisionBlock.getBounds().x;
        float colRight = collisionBlock.getBounds().x + collisionBlock.getWidth();
        float actorLeft = player.getBounds().x;
        float actorRight = player.getBounds().x + player.getWidth();

        float colBottom = collisionBlock.getBounds().y;
        float colRear = collisionBlock.getBounds().y + collisionBlock.getHeight();
        float actorBottom = player.getBounds().y;
        float actorRear = player.getBounds().y + player.getHeight();

        if (colLeft < actorRight && colRight > actorLeft && colBottom < actorRear && colRear > actorBottom) {
            if (player.getVelocity().x != 0) {
                float overlap = player.getVelocity().x < 0 ? colRight - actorLeft : colLeft - actorRight;
                player.getPosition().x += overlap;
                player.getVelocity().x -= player.getVelocity().x;
            }
            //if (player.getVelocity().y != 0) {
            //    float overlap = player.getVelocity().y < 0 ? colRear - actorBottom : colBottom - actorRear;
            //    if (player.getVelocity().y < 0) canJump = true;
            //    player.getPosition().y += overlap;
            //    player.getVelocity().y -= player.getVelocity().y;
            //}
        }
    }*/
}