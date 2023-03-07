<<<<<<< HEAD
<<<<<<< HEAD:client/core/src/org/knalpot/knalpot/Player/PlayerProcessor.java
package org.knalpot.knalpot.Player;
=======
=======
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c
package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.world.*;
<<<<<<< HEAD
>>>>>>> c148d34 (Refactored a bit folders in project):client/core/src/org/knalpot/knalpot/actors/PlayerProcessor.java
=======
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

import java.lang.Math;

import com.badlogic.gdx.Gdx;
<<<<<<< HEAD
<<<<<<< HEAD:client/core/src/org/knalpot/knalpot/Player/PlayerProcessor.java
import com.badlogic.gdx.math.Rectangle;
import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Addons.Constants;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.World.World;
=======
>>>>>>> c148d34 (Refactored a bit folders in project):client/core/src/org/knalpot/knalpot/actors/PlayerProcessor.java
=======
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

public class PlayerProcessor {
	private World world;
	private Player player;
<<<<<<< HEAD
    //
=======

>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c
	private final float SPEED = 120f;
    private final float JUMP_HEIGHT = 320f;

    private final float ACCELERATION = 12f;
    private final float DECCELERATION = 16f;
    private final float VELOCITY_POWER = 0.9f;

    private float moveInput;

    // Implementing jump mechanics
    private boolean canJump = true;

    // Gravity related.
    private float gravityForce = Constants.GRAVITY_FORCE;

	public PlayerProcessor(World world) {
		this.world = world;
		player = this.world.getPlayer();
	}

	public void update(float dt) {
		gravity();
        windowCollision();
        horizontalMovement();
        verticalMovement();

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
            player.getVelocity().x = 0f;
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
        float speedDifference = targetSpeed - player.getVelocity().x; // Difference between current desired velocity.
        float accelerationRate = (Math.abs(targetSpeed) > 0.01f) ? ACCELERATION : DECCELERATION;
        float movement = (float) Math.pow(Math.abs(speedDifference) * accelerationRate, VELOCITY_POWER) * Math.signum(speedDifference);

        player.getAcceleration().x = (int) movement;
    }

    private void jump() {
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
}