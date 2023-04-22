package org.knalpot.knalpot.addons.effects;

import org.knalpot.knalpot.addons.effects.OSM.Shape;

import com.badlogic.gdx.Gdx;

/**
 * 
 */
public class OSMAnimator {
    private OSM osm;

    private float[] size;

    private float timer;
    private float lineWidth;

    private boolean convertationIsDone;

    /**
     * {@link org.knalpot.knalpot.addons.effects.OSMAnimator OSMAnimator} constructor.
     * Takes {@link org.knalpot.knalpot.addons.effects.OSM OSM} as a parameter to
     * 
     * @param osm
     */
    public OSMAnimator(OSM osm) {
        this.osm = osm;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public boolean isConvertationDone() {
        return convertationIsDone;
    }

    public void setConvertation(boolean isDone) {
        convertationIsDone = isDone;
    }

    public void resetTimer() {
        timer = 0f;
    }

    public void switchShape(Shape shape) { 
        osm.setShape(shape);
    }

    public void setSize(float[] size) {
        this.size = size;
    }

    public void updateOSMSize() {
        osm.setSize(new float[size.length]);
    }

    public void increaseLineWidth(float widthLimit, float millis) {
        // Updating line width according to the timer. (smooth increase)
        if (lineWidth < widthLimit) {
            timer += Gdx.graphics.getDeltaTime();
            if (timer >= millis) {
                timer -= millis;
                lineWidth += 1;
            }
        }
    }

    public void decreaseLineWidth(float millis) {
        if (lineWidth > 0f) {
            timer += Gdx.graphics.getDeltaTime();
            if (timer >= millis) {
                timer -= millis;
                lineWidth -= 1;
            }
        }
    }

    /**
     * Expands rectangle step-by-step: first Ox, then Oy.
     * @return float[]
     */
    private float[] expandRectByStep() {
        float[] changedSize = new float[size.length];

        changedSize[0] = osm.getSize()[0];
        changedSize[1] = osm.getSize()[1];

        // Changing size first horizontally,
        // then vertically.
        if (osm.getSize()[0] < size[0]) {
            changedSize[0] = osm.getSize()[0] + size[0] / 10;
        } else {
            changedSize[1] = osm.getSize()[1] + size[1] / 10;
        }

        return changedSize;
    }

    private float[] shrinkRectByStep() {
        float[] changedSize = new float[size.length];

        changedSize[0] = osm.getSize()[0];
        changedSize[1] = osm.getSize()[1];

        // Changing horizontally,
        // then vertically.
        if (osm.getSize()[1] > 0) {
            changedSize[1] = osm.getSize()[1] - size[1] / 10;
        } else {
            changedSize[0] = osm.getSize()[0] - size[0] / 10;
        }

        return changedSize;
    }

    /**
     * Expands to required shape. Used for smooth transition from
     * one shape to another.
     * @param millis
     */
    public void expandShape(float millis) {
        float[] changedSize = new float[size.length];
        timer += Gdx.graphics.getDeltaTime();

        if (timer >= millis) {
            switch (osm.getShape()) {
                case RECT:
                    changedSize = expandRectByStep();
                    break;
                case CIRCLE:
                    if (osm.getSize()[0] + 1 == size[0])
                        convertationIsDone = true;
                    changedSize[0] = osm.getSize()[0] + 1f;
                    break;
                case POLY:
                    break;
                default:
                    break;
            }

            timer -= millis;
            osm.setSize(changedSize);
        }
    }

    /**
     * Shrinks shape of the object. Will mainly be used for animating
     * transitions from one shape to another.
     * @param millis
     */
    public void shrinkShape(float millis) {
        float[] changedSize = new float[osm.getSize().length];
        timer += Gdx.graphics.getDeltaTime();

        if (timer >= millis) {
            if (osm.getShape() == Shape.RECT)
                changedSize = shrinkRectByStep();
            else {
                for (int i = 0; i < osm.getSize().length; i++) {
                    if (osm.getSize()[i] == 0) continue;
                    changedSize[i] = osm.getSize()[i] - 1f;
                }
            }

            timer -= millis;
            osm.setSize(changedSize);
        }
    }

    public void drawPolygonOverObject(float[] verticles) {
        osm.drawPoly(verticles, lineWidth);
    }
}
