package org.knalpot.knalpot.actors.player;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.interactive.props.Chest;
import org.knalpot.knalpot.interactive.props.Consumable;
import org.knalpot.knalpot.world.World;

import java.lang.Math;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    // ==== MOVEMENT ==== //
	private final float SPEED = 120f;
    private final float JUMP_HEIGHT = 400f;
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

    // ==== HUD ==== //
    private boolean isHUDActive = false;

    // ==== ENEMY COLLISIONS ==== //
    private float attackTimer = 2f;
    //#endregion
    
    //#region -- FUNCTIONS --
	/**
     * Processor constructor.
	 * @param world
	 */
	public PlayerProcessor(World world) {
		this.world = world;
		player = this.world.getPlayer();
	}

	/**
     * Updating {@code Player}'s position each frame.
	 * @param dt
	 */
	public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
			isHUDActive = !isHUDActive;
		}

        // System.out.println("-----");
		gravity();
        windowCollision(dt);
        if (!isHUDActive) {
            horizontalMovement();
            verticalMovement();
        }
        changeState();
        isAttacked();

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
        

        for (Static obj : world.collisionBlocks) {
            if (resolveCollision(player, obj, dt)) {
                // System.out.println("Colliding!");
                if (player.getVelocity().y == 0f) canJump = true;
            }
        }

        for (Static obj : world.platforms) {
            if (resolvePlatformCollision(player, obj, dt)) {
                if (player.getVelocity().y == 0f) canJump = true;
            }
        }

        for (Chest chest : world.getChest()) {
            if (player.getBounds().overlaps(chest.getBounds()) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                chest.getHUD().changeActive();
                ((Player) player).chestIndex = world.getChest().indexOf(chest);
            }
        }

        player.previousDirection = player.direction;
        player.direction = moveInput;
        player.update(dt);
        // System.out.println("-----");

        // HUD and inventory stuff // temporary
        takeConsumableFromChest();
        useCurrentConsumable();
        ((Player) player).getHud().setIsActive(isHUDActive);
        ((Player) player).getHud().update(dt);
	}

    private void useCurrentConsumable() {
        if (isHUDActive) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                Consumable consumable = ((Player) player).getInventory().get(((Player) player).getHud().getCurrentConsum());
                player.changeHealth(consumable.getPower());
                ((Player) player).removeConsumable(consumable);
            }
        }
    }

    private void takeConsumableFromChest() {
        if (world.getChest().size() != 0) {
            Chest chest = world.getChest().get(((Player) player).chestIndex);
            if (chest.getHUD().getIsActive()) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    Consumable consumable = chest.getConsumableOfIndex(chest.getHUD().getCurrentConsum());
                    chest.getHUD().decreaseCurrentConsum();
                    ((Player) player).addConsumable(consumable);
                    chest.removeConsumable(consumable);
                }
            }
        }
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
            move();
        }
        if (isRightPressed) {
            moveInput = 1;
            move();
        }
        if (((!isLeftPressed && !isRightPressed) || (isLeftPressed && isRightPressed))) {
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
    	player.getVelocity().y = JUMP_HEIGHT;
    }
    //#endregion

    /**
     * Changes player state according to its
     * velocity data.
     */
    private void changeState() {
        if (player.getVelocity().x == 0 && player.getVelocity().y == 0) {
            updateState();
            player.state = Player.State.IDLE;
        }

        if (player.getVelocity().x != 0f) {
            updateState();
            player.state = Player.State.MOVE;
        }

        if (player.getVelocity().y > 0) {
            updateState();
            player.state = Player.State.JUMP;
        }
        if (player.getVelocity().y < 0) {
            updateState();
            player.state = Player.State.FALL;
        }
    }

    private void isAttacked() {
        ListIterator<Enemy> enemyIterator = world.getEnemies().listIterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.getBounds().overlaps(player.getBounds())) {
                attackTimer -= Gdx.graphics.getDeltaTime();
                if (attackTimer <= 0f) {
                    ((Player) player).caughtByEnemy(10);
                    attackTimer = 2f;
                }
            }
        }
    }

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

    private boolean resolvePlatformCollision(Actor in, Static platform, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (player.DynamicAABB(in, platform, cp, cn, contactTime, dt)) {
            cn = player.getContactNormal();
            cp = player.getContactPoint();
            t = player.getContactTime();

            if (in.getVelocity().y < 0f) {
                in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            }
            return true;
        }
        return false;
    }
    //#endregion
}