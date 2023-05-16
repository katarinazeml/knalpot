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
    private int CHASE_RADIUS = 50;

    public Game world;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        health = 100;
    }
    
    public void update() {
        for (Actor player : world.getPlayers().values()) {
            // float distanceToPlayer = Math.abs(player.x - x);
            chasePlayer = player.x >= (x - CHASE_RADIUS) && player.x <= (x + CHASE_RADIUS);
            System.out.println("Collision data");
            System.out.println(player.x);
            System.out.println(this.x);

            if (chasePlayer) {
                System.out.println("Player is inside the radius");
                // Player is within chase radius, move towards player
                int moveTo = (player.x < x) ? -1 : 1;
                x += spd * 0.01 * moveTo;
                // this.direction = moveTo;
            }
        }

        // if (!chasePlayer) {
        //     dt += 0.1;

        //     if (dt >= 10) {
        //         direction = ThreadLocalRandom.current().nextInt(-1, 2);
        //         spd = ThreadLocalRandom.current().nextInt(30, 40);
        //         dt -= 10;
        //     }

        //     x += spd * direction * 0.01;
        // }
    }
}
