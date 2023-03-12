package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.interactive.CollisionBlock;
import org.knalpot.knalpot.world.*;

import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class PlayerProcessor {
	private World world;
	private Player player;
    private CollisionBlock collisionBlock;

    // ===== ALL COMMENTED OUT CODE IS TEMPORARY DISABLED ===== //
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
        collisionBlock = this.world.getCollisionBlocks();
	}

	public void update(float dt) {
        System.out.println("Player X position");
        System.out.println(player.getPosition().x);
        System.out.println("Player Y pos");
        System.out.println(player.getPosition().y);
        
        // Stupid movement mechanic just for the sake of
        // changing velocity so I can quicker adapt it to
        // the normal mechanic.
        // if (Gdx.input.isKeyPressed(Constants.UP_KEY)) {
        //     player.getVelocity().y = SPEED;
        // }
        // if (Gdx.input.isKeyPressed(Constants.DOWN_KEY)) {
        //     player.getVelocity().y = -SPEED;
        // }
        // if (Gdx.input.isKeyPressed(Constants.RIGHT_KEY)) {
        //     player.getVelocity().x = SPEED;
        // }
        // if (Gdx.input.isKeyPressed(Constants.LEFT_KEY)) {
        //     player.getVelocity().x = -SPEED;
        // }
        // if (!Gdx.input.isKeyPressed(Constants.UP_KEY)
        //     && !Gdx.input.isKeyPressed(Constants.DOWN_KEY)
        //     && !Gdx.input.isKeyPressed(Constants.RIGHT_KEY)
        //     && !Gdx.input.isKeyPressed(Constants.LEFT_KEY)) {
        //     player.getVelocity().x = 0;
        //     player.getVelocity().y = 0;
        // }

        // player.update(dt);

        // if (RectvsRect(player, collisionBlock)) {
        //     System.out.println("Colliding!");
        // }
		gravity();
        windowCollision(dt);
        horizontalMovement();
        verticalMovement();
        testCollide(dt);

    	player.getAcceleration().scl(dt);
        System.out.println("scalar Y accel:");
        System.out.println(player.getAcceleration().y);
		player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);
        System.out.println("position X:");
        System.out.println(player.getPosition().x);
        System.out.println("position Y:");
        System.out.println(player.getPosition().y);
        System.out.println("bounds");
        System.out.println(player.getBounds().x);
        player.update(dt);
	}

    // Pure AABB without responses.
    // private boolean RectvsRect(Player rect1, CollisionBlock rect2) {
    //     return rect1.Left < rect2.Right
    //         && rect1.Right > rect2.Left
    //         && rect1.Bottom < rect2.Top
    //         && rect1.Top > rect2.Bottom;
    // }

    // private boolean RayvsRect() {
    //     return false;
    // }


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

        player.getAcceleration().x = movement;
    }

    private void jump() {
        canJump = false;
        player.state = Player.State.JUMP;
    	player.getVelocity().y = JUMP_HEIGHT;
    }

    private void windowCollision(float dt) {
        if (player.Left + player.getScalarVelocity(dt).x <= 200) {
            player.getPosition().x = 200f;
            player.getVelocity().x -= player.getVelocity().x;
        }
        if (player.Right + player.getScalarVelocity(dt).x >= 600) {
            player.getPosition().x = 600 - player.getWidth();
            player.getVelocity().x -= player.getVelocity().x;
        }
        if (player.Bottom + player.getScalarVelocity(dt).y <= 0) {
            canJump = true;
            player.state = Player.State.IDLE;
        	player.getPosition().y = 0f;
            player.getVelocity().y -= player.getVelocity().y;
        }
        if (player.Top + player.getScalarVelocity(dt).y >= 480 - player.getHeight()) player.getVelocity().y -= player.getVelocity().y;
    }

    private void testCollide(float dt) {
        if (isTouchingLeft(dt)) {
           player.getVelocity().x -= player.getVelocity().x;
           player.getPosition().x = collisionBlock.Right - player.getScalarVelocity(dt).x;
        }
        // if (isTouchingBottom(dt)) {
        //     canJump = true;
        //     player.getVelocity().y -= player.getVelocity().y;
        //     player.getPosition().y = collisionBlock.Top - player.getScalarVelocity(dt).y;
        // }
    }

    protected boolean IsTouchingRight(float dt) {
      return player.Right + player.getScalarVelocity(dt).x > collisionBlock.Left &&
        player.Left < collisionBlock.Left;
    }

    protected boolean isTouchingLeft(float dt) {
        return player.Left + player.getScalarVelocity(dt).x < collisionBlock.Right
            && player.Right > collisionBlock.Right && !isTouchingBottom(dt) && !isTouchingTop(dt);
    }

    protected boolean isTouchingBottom(float dt) {
        return player.Bottom + player.getScalarVelocity(dt).y < collisionBlock.Top
            && player.Top > collisionBlock.Top;
    }

    protected boolean isTouchingTop(float dt) {
        return player.Top + player.getScalarVelocity(dt).y > collisionBlock.Bottom
            && player.Bottom < collisionBlock.Bottom;
    }
}