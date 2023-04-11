package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.actors.Player.State;
import org.knalpot.knalpot.interactive.Static;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * {@code Actor} class is used by any dynamic element: player, treasure chest, bullet etc.
 * It includes the base for dynamic object such as variables, getters and mechanisms.
 * <p>
 * Created because game architecture must be flexible and clean. Don't complain about it, please.
 * @author Max Usmanov
 * @version 0.1
 */
public class Actor {
    //#region -- VARIABLES --

    // ==== TEXTYRES ==== //
    protected Texture texture;
    protected int[] BBSize;
    protected int scaleSize;

    // ==== BOUNDS ==== //
    protected Rectangle bounds;

    // POSITION BOUNDS //
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;

    // ==== SPRITE RELATED VARIABLES ==== //
    public int direction;
    public int previousDirection;

    // ==== SIZE ==== //
    protected int WIDTH;
    protected int HEIGHT;
    
    // ==== VECTOR-BASED MOVEMENT VARIABLES ==== //
    protected Vector2 position;
    protected Vector2 acceleration;
    protected Vector2 velocity;

    // ==== COLLISION DETECTION VARIABLES ==== //
    private Vector2 cp; // contact point
    private Vector2 cn; // contact normal
    private float t; // time collision has happened

    // ==== TEMPORARY STATE MACHINE ==== //
    public State state;
    public State previousState;

    //#endregion
    //#region -- GET/SET --
    // ==== GETTERS ==== //
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }
    
    public Vector2 getVelocity() {
        return velocity;
    }
    
    public Vector2 getScalarVelocity(float dt) {
        return velocity.cpy().scl(dt);
    }

    public  Vector2 getContactPoint() {
        return cp;
    }

    public Vector2 getContactNormal() {
        return cn;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getScale() {
        return scaleSize;
    }

    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }

    public float getContactTime() {
        return t;
    }

    // ==== SETTERS ==== //
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    //#endregion

    //#region -- FUNCTIONALITY --
    /**
     * Updates {@code Actor}'s data like positions, velocity and etc.
     * @param dt
     */
    public void update(float dt) { }

    /**
     * Helper method for swapping X and Y vector coordinates.
     * @param x
     * @param f
     * @return
     */
    private static float swap(float x, float f) {
        return x;
    }

    //#region -- COLLISIONS --

    /* ##### NB! 
     * ##### THE FOLLOWING CODE IS FULLY EXPERIMENTAL. IT REQUIRES BASIC KNOWLEDGE OF VECTORS AND THEIR PROEJECTIONS #####
     * ##### IT HAS A TON OF BUGS WHICH COULD BE FIXED BUT I AM QUITE LAZY TO DO SO.                                 #####
     * ##### IF YOU HAVE ANY QUESTIONS, PLEASE DON'T.                                                                #####
    */

    /**
     * Returns {@code true} if ray's nearest contact point with {@code Static} 
     * fits certain conditions (which are written in code).
     * Otherwise returns {@code false}.
     * @param rayOrigin
     * @param rayDirection
     * @param block
     * @param contactPoint
     * @param contactNormal
     * @param timeHitNear
     * @return
     */
    private boolean RayAABB(Vector2 rayOrigin, Vector2 rayDirection, Rectangle block, 
        Vector2 contactPoint, Vector2 contactNormal, float tHitNear) {
        
        contactPoint = new Vector2();
        contactNormal = new Vector2();
        
        Vector2 tNear = new Vector2();
        Vector2 tFar = new Vector2();
        tNear.x = (block.getX() - rayOrigin.x) / rayDirection.x;
        tNear.y = (block.getY() - rayOrigin.y) / rayDirection.y;
        tFar.x = ((block.getX() + block.getWidth()) - rayOrigin.x) / rayDirection.x;
        tFar.y = ((block.getY() + block.getHeight()) - rayOrigin.y) / rayDirection.y;

        // System.out.println("-------");
        // System.out.println("t near | t far");
        // System.out.println(tNear);
        // System.out.println(tFar);
        // System.out.println(" ");

        if (tNear.x > tFar.x) tFar.x = swap(tNear.x, tNear.x=tFar.x);
        if (tNear.y > tFar.y) tFar.y = swap(tNear.y, tNear.y=tFar.y);
        // System.out.println("updated t near | t far");
        // System.out.println(tNear);
        // System.out.println(tFar);
        // System.out.println(" ");

        if (tNear.x > tFar.y || tNear.y > tFar.x) return false;

        tHitNear = Math.max(tNear.x, tNear.y);
        float tHitFar = Math.min(tFar.x, tFar.y);
        // System.out.println("hit near | hit far");
        // System.out.println(tHitNear);
        // System.out.println(tHitFar);
        // System.out.println(" ");

        if (tHitNear > 1f) return false;
        if (tHitFar < 0f) return false;

        contactPoint.x = rayOrigin.x + (tHitNear * rayDirection.x);
        contactPoint.y = rayOrigin.y + (tHitNear * rayDirection.y);
        // System.out.println("contact point");
        // System.out.println(contactPoint);

        if (tNear.x > tNear.y) {
            if (rayDirection.x > 0) contactNormal = new Vector2(1, 0);
            else contactNormal = new Vector2(-1, 0);
        }
        else if (tNear.x < tNear.y) {
            if (rayDirection.y > 0) contactNormal = new Vector2(0, 1);
            else contactNormal = new Vector2(0, -1);
        }

        // System.out.println("contact normal");
        // System.out.println(contactNormal);

        // System.out.println("Saving calculated variables into static ones");
        cn = contactNormal;
        cp = contactPoint;
        t = tHitNear;

        return true;
    }

    /**
     * Basically returns {@code true} if {@code RayAABB} returns {@code true}. 
     * Uses expanded rectangle bounds for easier collision detection.
     * @param in
     * @param target
     * @param contactPoint
     * @param contactNormal
     * @param contactTime
     * @param dt
     * @return
     */
    public boolean DynamicAABB(Actor in, Static target, Vector2 contactPoint, Vector2 contactNormal, float contactTime, float dt) {
        if (in.getVelocity().x == 0 && in.getVelocity().y == 0) return false;
        
        Rectangle expandedTarget = new Rectangle();
        expandedTarget.x = target.getPosition().x - (in.getBounds().width / 2);
        expandedTarget.y = target.getPosition().y - (in.getBounds().height / 2);
        expandedTarget.width = target.getWidth() + in.getBounds().width;
        expandedTarget.height = target.getHeight() + in.getBounds().height;

        Vector2 dynamicRectPos = new Vector2();
        dynamicRectPos.x = in.getBounds().x + (in.getBounds().width / 2);
        dynamicRectPos.y = in.getBounds().y + (in.getBounds().height / 2);

        // System.out.println("expanded target parameters");
        // System.out.println(expandedTarget.getX() + ":" + expandedTarget.getY());
        // System.out.println(expandedTarget.getWidth() + ":" + expandedTarget.getHeight());

        // System.out.println("Dynamic rectangle position");
        // System.out.println(dynamicRectPos.x);
        // System.out.println(dynamicRectPos.y);

        if (RayAABB(dynamicRectPos, in.getVelocity().cpy().scl(dt), expandedTarget, contactPoint, contactNormal, contactTime)) {
            return true;
        }
        return false;
    }
    //#endregion
    //#endregion

    public void render(SpriteBatch batch) { }
}
