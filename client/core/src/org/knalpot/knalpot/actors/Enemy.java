package org.knalpot.knalpot.actors;

import java.util.ArrayList;
import java.util.EnumMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;

public class Enemy extends Actor {

    public enum EnemyState {
        IDLE, MOVE, JUMP, ATTACK
    }

    private ArrayList<EnemyBullet> bullets;

    private EnemyState enemyState;

    private float rotation;

    private Player player;

    public Enemy(Vector2 position, Player player) {
        this.position = position;
        this.player = player;
        BBSize = new int[]{60, 60};
        scaleSize = 2;
        bounds = new Rectangle(position.x, position.y, BBSize[0] * scaleSize, BBSize[1] * scaleSize);
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        bullets = new ArrayList<>();
    }

    @Override
    public void update(float dt) {
        // Update enemy position using the Actor class's position
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;

        // Calculate the angle between the player position and the enemy's position
        float angle = (float) MathUtils.atan2(player.getPosition().y - position.y,
        player.getPosition().x - position.x);

        // Convert the angle to degrees and subtract 90 degrees to account for the orientation of the orb's sprite
        rotation = (angle * MathUtils.radiansToDegrees + 270) % 360;

        // if (enemyState == EnemyState.ATTACK) {
        //     System.out.println(enemyState);
        //     shoot(dt, rotation);
        // }
        shoot(dt, rotation);
        for (EnemyBullet bullet : bullets) {
            bullet.update(dt);
        }
    }

    public void shoot(float dt, float angle) {
        bullets.add(new EnemyBullet(this, angle));
    }

    public void setEnemyDirection(int direction) {
        this.direction = direction;
    }

    public int getEnemyDirection() {
        return this.direction;
    }

    public void setState(EnemyState state) {
        this.enemyState = state;
    }
    
    public EnemyState getState() {
        return enemyState;
    }

    public ArrayList<EnemyBullet> getEnemyBullets() {
        return bullets;
    }
}
