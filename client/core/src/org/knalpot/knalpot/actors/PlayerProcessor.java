package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.world.World;

import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

// ===== ALL COMMENTED OUT CODE IS REQUIRED FOR DEBUGGING BUT USELESS AS FOR NOW. DON'T PAY ATTENTION TO IT ===== //

/**
 * {@code PlayerProcessor} is responsible for handling player's actions: movement, gravitation, interaction.
 * It doesn't do anything besides <b>processing</b> data accordingly. Seriously.
 * @author Max Usmanov
 * @version 0.1
 */
public class PlayerProcessor {
    //#region -- VARIABLES --

    // ==== OBJECT VARIABLES ==== //
	private World world;
	private Actor player;
    private Static collisionBlock;

    // ==== MOVEMENT ==== //
	private final float SPEED = 120f;
    private final float JUMP_HEIGHT = 320f;
    private final float ACCELERATION = 12f;
    private final float DECCELERATION = 16f;
    private final float VELOCITY_POWER = 0.9f;

    private int moveInput = 1;

    // ==== JUMP MECHANICS ==== //
    private boolean canJump = true;

    // ==== GRAVITY ==== //
    private float gravityForce = Constants.GRAVITY_FORCE;

    // ==== COLLISION-RELATED ==== //
    private Vector2 cp;
    private Vector2 cn;
    private float t;
    //#endregion
    
    //#region -- FUNCTIONS --
	/**
     * Processor constructor.
	 * @param world
	 */
	public PlayerProcessor(World world) {
		this.world = world;
		player = this.world.getPlayer();
        collisionBlock = this.world.getCollisionBlocks();
	}

	/**
     * Updating {@code Player}'s position each frame.
	 * @param dt
	 */
	public void update(float dt) {
        // System.out.println("-----");
		gravity();
        windowCollision(dt);
        horizontalMovement();
        verticalMovement();

    	player.getAcceleration().scl(dt);
        // System.out.println("scalar Y accel:");
        // System.out.println(player.getAcceleration().y);
		player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);
        // System.out.println("bounds");
        // System.out.println(player.getBounds().x);
        // System.out.println("Player X position");
        // System.out.println(player.getPosition().x);
        // System.out.println("Player Y pos");
        // System.out.println(player.getPosition().y);

        // System.out.println("colblock position");
        // System.out.println(collisionBlock.getPosition());
        // System.out.println("Important stuff for collisions");
        // System.out.println(cn);
        // System.out.println(cp);
        // System.out.println(t);

        if (resolveCollision(player, collisionBlock, dt)) {
            // System.out.println("Colliding!");
            if (player.getVelocity().y == 0f) canJump = true;
        }

        player.previousDirection = player.direction;
        player.direction = moveInput;
        player.update(dt);
        // System.out.println("-----");
	}

	/**
	 * Adds constant gravity force to object.
	 */
	private void gravity() {
		if (player.getVelocity().y < 0) gravityForce = Constants.GRAVITY_FORCE * Constants.GRAVITY_ACCEL;
        player.getAcceleration().y = -gravityForce;
	}

    private void updateState() {
        player.previousState = player.state;
    }

    //#region - MOVEMENT -
    /**
     * Moves {@code Player} horizontally using specified keys.
     */
    private void horizontalMovement() {
        boolean isLeftPressed = Gdx.input.isKeyPressed(Constants.LEFT_KEY);
        boolean isRightPressed = Gdx.input.isKeyPressed(Constants.RIGHT_KEY);
        if (isLeftPressed) {
            moveInput = -1;
            updateState();
            player.state = Player.State.MOVE;
            move();
        }
        if (isRightPressed) {
            moveInput = 1;
            updateState();
            player.state = Player.State.MOVE;
            move();
        }
        if (((!isLeftPressed && !isRightPressed) || (isLeftPressed && isRightPressed))) {
            updateState();
            player.state = Player.State.IDLE;
            player.getVelocity().x = 0f;
        }
    }

	/**
	 * Moves {@code Player} vertically using specified keys.
	 */
	private void verticalMovement() {
        boolean isSpacePressed = Gdx.input.isKeyJustPressed(Constants.SPACEBAR);

        if (isSpacePressed && canJump) {
        	jump();
        }
	}

	/**
	 * Moves {@code Player} using mathematical equations for smooth acceleration.
	 */
	private void move() {
        // These variables are taken from YouTube tutorial on smooth platformer movements.
        float targetSpeed = moveInput * SPEED; // Direction of movement.
        float speedDifference = targetSpeed - player.getVelocity().x; // Difference between current desired velocity.
        float accelerationRate = (Math.abs(targetSpeed) > 0.01f) ? ACCELERATION : DECCELERATION;
        float movement = (float) Math.pow(Math.abs(speedDifference) * accelerationRate, VELOCITY_POWER) * Math.signum(speedDifference);

        player.getAcceleration().x = movement;
    }

    /**
     * Makes {@code Player} jump.
     */
    private void jump() {
        canJump = false;
        // player.state = Player.State.JUMP;
    	player.getVelocity().y = JUMP_HEIGHT;
    }
    //#endregion

    //#region - COLLISIONS -
    /**
     * Temporary collision for window borders. Will be removed in the nearest future.
     * @param dt
     */
    private void windowCollision(float dt) {
        if (player.Bottom + player.getScalarVelocity(dt).y <= 0) {
            canJump = true;
        	player.getPosition().y = 0f;
            player.getVelocity().y -= player.getVelocity().y;
        }
        if (player.Top + player.getScalarVelocity(dt).y >= 480 - player.getHeight()) player.getVelocity().y -= player.getVelocity().y;
    }

    /**
     * Resolves AABB collision using {@code Actor}'s physics.
     * @param in
     * @param block
     * @param dt
     * @return boolean
     */
    private boolean resolveCollision(Actor in, Static block, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (player.DynamicAABB(in, block, cp, cn, contactTime, dt)) {
            cn = player.getContactNormal();
            cp = player.getContactPoint();
            t = player.getContactTime();
            // System.out.println("checking velocity");
            // System.out.println(in.getVelocity().x + ":x before");
            in.getVelocity().x -= cn.x * Math.abs(in.getVelocity().x) * (1 - contactTime);
            // System.out.println(in.getVelocity().x + ":x after");
            // System.out.println(in.getVelocity().y + ":y before");
            in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            // System.out.println(in.getVelocity().y + ":y after");
            return true;
        }
        return false;
    }
    //#endregion

    //#endregion
}