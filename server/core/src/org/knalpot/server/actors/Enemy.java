package org.knalpot.server.actors;

import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.server.Game;

public class Enemy {

    public float x, y;
    public int id;
    public int direction = 1;
    public int health;
    
    private double dt;
    private int spd = 35;

    private boolean chasePlayer;
    private int CHASE_RADIUS = 150;

    public Game world;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        health = 100;
    }
    
    public void update() {
        System.out.println(world.getPlayers().size());
        for (Actor player : world.getPlayers().values()) {
            // float distanceToPlayer = Math.abs(player.x - x);
            chasePlayer = (player.x > (this.x - CHASE_RADIUS) * 2 && player.x < (this.x + CHASE_RADIUS) * 2)
                || player.y < (this.y - CHASE_RADIUS) * 2 && player.y > (this.y + CHASE_RADIUS) * 2;

            if (chasePlayer) {
                System.out.println("Player is inside the radius");
                System.out.println("Collision data");
                System.out.println(player.x);
                System.out.println(this.x);
                System.out.println(chasePlayer);

                // Player is within chase radius, move towards player
                int moveToX = (player.x < this.x * 2) ? -1 : 1;
                int moveToY = (player.y < this.y * 2) ? -1 : 1;
                this.x += spd * 0.01 * moveToX;
                this.y += spd * 0.01 * moveToY;
            }
        }

        if (!chasePlayer) {
            dt += 0.1;

            if (dt >= 10) {
                direction = ThreadLocalRandom.current().nextInt(-1, 2);
                spd = ThreadLocalRandom.current().nextInt(30, 40);
                dt -= 10;
            }

            x += spd * direction * 0.01;
        }
    }
}
