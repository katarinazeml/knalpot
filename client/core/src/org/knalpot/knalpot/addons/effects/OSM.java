package org.knalpot.knalpot.addons.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>{@link org.knalpot.knalpot.addons.effects.OSM OSM}, as name says, dynamically
 * manipulates shapes of objects. Its main purpose is to get rid of texture dependency
 * for multiple objects, such as {@link org.knalpot.knalpot.actors.orb.Orb Orb} and
 * {@link org.knalpot.knalpot.actors.Bullet Bullet}.</p>
 * 
 * This method provides flexibility to their visuals' manipulation.
 * 
 * @author Maksim Usmanov
 * @version 0.1
 */
public class OSM {
    public enum Shape {
        CIRCLE,
        RECT,
        POLY,
    }

    private Shape shape;

    private ShapeRenderer renderer;
    private ShapeType type;
    private Color color;

    private Vector2 position;
    
    // General properties
    private float[] size;
    private float LINE_WIDTH;
    private boolean transparency = false;
    
    // When there must be multiple layers around the
    // shape, set it to true and specify amount of layers.
    private boolean layers = false;
    private int layerNum;
    
    public OSM(Shape shape, ShapeType type, Color color) {
        this.shape = shape;
        this.type = type;
        this.color = color;

        renderer = new ShapeRenderer();
    }

    public OSM(Shape shape, ShapeType type, Color color, Vector2 position, float[] size) {
        this.shape = shape;
        this.type = type;
        this.color = color;
        this.position = position;
        this.size = size;

        renderer = new ShapeRenderer();
    }

    public Shape getShape() {
        return shape;
    }

    public ShapeRenderer getRenderer() {
        return renderer;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float[] getSize() {
        return size;
    }

    public void setSize(float[] size) {
        this.size = size;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setLineWidth(float lineWidth) {
        this.LINE_WIDTH = lineWidth;
    }

    public void enableTransparency() {
        this.transparency = true;
    }

    public void disableTransparency() {
        this.transparency = false;
    }

    public void enableLayers(int layerNum) {
        this.layers = true;
        this.layerNum = layerNum;
    }

    public void disableLayers() {
        this.layers = false;
        this.layerNum = 0;
    }

    public void drawCircle() {
        if (layers) {
            for (int i = 2; i < layerNum + 2; i++) {
                renderer.begin(type);
                renderer.setColor(129 / 255f, 161 / 255f, 193 / 255f, 1f - (0.2f * i));
                renderer.circle(position.x, position.y, size[0] * i * 0.8f);
                renderer.end();
            }
        }

        renderer.begin(type);
        renderer.setColor(color);
        renderer.circle(position.x, position.y, size[0]);
        renderer.end();
    }

    public void drawRect() {
        renderer.begin(type);
        renderer.rect(position.x, position.y, size[0], size[1]);
        renderer.end();
    }

    public void drawRect(float x, float y, float width, float height, Color color, boolean isTransparent) {
        if (isTransparent) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        renderer.setColor(color);
        renderer.begin(ShapeType.Filled);
        renderer.rect(x, y, width, height);
        renderer.end();
    } 

    public void drawPoly() {
        renderer.begin(ShapeType.Line);
        renderer.polygon(size);
        renderer.end();
    }

    public void drawPoly(float[] verticles, float lineWidth) {     
        Gdx.gl.glLineWidth(lineWidth);
        renderer.begin(ShapeType.Line);
        renderer.polyline(verticles);
        renderer.end();
    }


    public void render() {
        Gdx.gl.glLineWidth(LINE_WIDTH);
        if (transparency) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        renderer.setColor(color);

        if (shape == Shape.CIRCLE) {
            drawCircle();
        }

        if (shape == Shape.RECT) {
            drawRect();
        }

        if (shape == Shape.POLY) {
            drawPoly();
        }
    }

    public void render(SpriteBatch batch) {
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        render();
    }

    public void dispose() {
        renderer.dispose();
    }
}
