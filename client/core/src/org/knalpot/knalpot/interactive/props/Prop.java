package org.knalpot.knalpot.interactive.props;

import org.knalpot.knalpot.interactive.Static;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Prop class.
 * @author Maksim Usmanov
 * @version 0.1
 */
public class Prop extends Static {
    public enum PropType {
        POTION,
        USELESS
    }

    private String name;
    private PropType type;
    private int power;
    private float scale;

    private Vector2 targetPos;

    public Prop(Vector2 position, int width, int height, Texture texture) {
        super(position, width, height, texture);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropType getType() {
        return type;
    }

    public void setType(PropType type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Vector2 getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(float x, float y) {
        if (targetPos == null) targetPos = new Vector2();
        targetPos.x = x;
        targetPos.y = y;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, position.x - getWidth() / 2, position.y, getWidth() * scale, getHeight() * scale);
        batch.end();
    }
}
