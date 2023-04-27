package org.knalpot.knalpot.hud;

import java.util.List;

import org.knalpot.knalpot.addons.Constants;
import org.knalpot.knalpot.addons.effects.OSM;
import org.knalpot.knalpot.addons.effects.OSMAnimator;
import org.knalpot.knalpot.addons.effects.OSM.Shape;
import org.knalpot.knalpot.hud.HUD.HUDType;
import org.knalpot.knalpot.interactive.props.Prop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class HUDProcessor {
    // Specific type for future processing.
    private HUDType type;

    // OSM for rendering.
    private OSM osm;
    private OSMAnimator animator;

    // Typical data.
    private Vector2 position;
    private float[] size;
    private boolean isActive;

    // temporary inventory stuff.
    // this code is probably a bad idea but i need it
    // for testing purposes.
    private SpriteBatch batch;
    private List<Prop> inventory;
    private int currentProp = 0;
    private float speed = 4f;

    // Fonts.
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixeboy.ttf"));
    private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    private BitmapFont font;

    public HUDProcessor(HUDType type) {
        this.type = type;

        ShapeType shape;

        parameter.size = 24;
        font = generator.generateFont(parameter);
        font.getData().setScale(2);
        generator.dispose();

        switch(this.type) {
            case STATS:
                shape = ShapeType.Filled;
                position = new Vector2(10, 10);
                size = new float[] { 50, 30 };
                break;
            case INVENTORY:
                shape = ShapeType.Line;
                position = new Vector2(
                    Constants.WINDOW_WIDTH / 2 - Constants.WINDOW_WIDTH / 4, 
                    Constants.WINDOW_HEIGHT / 2 - Constants.WINDOW_HEIGHT / 4
                );

                size = new float[] { 
                    Constants.WINDOW_WIDTH / 2, 
                    Constants.WINDOW_HEIGHT / 2
                };

                batch = new SpriteBatch();
                break;
            default:
                shape = ShapeType.Filled;
                break;
        }

        osm = new OSM(Shape.RECT, shape, Color.LIGHT_GRAY);
        animator = new OSMAnimator(osm);
    }

    public int getCurrentProp() {
        return currentProp;
    }

    public void setOSMData() {
        osm.setPosition(position);
        animator.setSize(size);
        animator.setLineWidth(10f);
        animator.updateOSMSize();
    }

    public void initializeInventory(List<Prop> props) {
        this.inventory = props;
        for (int i = 0; i < inventory.size(); i++) {
            Prop prop = inventory.get(i);
            prop.setPosition(
                Constants.WINDOW_WIDTH / 2 - prop.getWidth() / 2 + prop.getWidth() * 3 * (i - currentProp), 
                Constants.WINDOW_HEIGHT / 2
            );
        }
    }

    public void setTargetPosition() {
        for (int i = 0; i < inventory.size(); i++) {
            Prop prop = inventory.get(i);
            prop.setTargetPos(
                Constants.WINDOW_WIDTH / 2 - prop.getWidth() / 2 + prop.getWidth() * 3 * (i - currentProp), 
                Constants.WINDOW_HEIGHT / 2
            );
        }
    }
    
    public void updatePropsPosition(float dt) {
        for (Prop prop : inventory) {
            prop.setScale(1f);

            if (prop.getTargetPos() != null) {
                float dx = prop.getTargetPos().x - prop.getPosition().x;

                if (inventory.indexOf(prop) == currentProp) prop.setScale(2 - Math.abs(dx / 100));
                prop.changePosition(dx * speed, 0, dt);
            }
        }
    }

    public void update(float dt, boolean isActive) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            currentProp += 1;
            currentProp %= inventory.size();
            setTargetPosition();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            currentProp -= 1;
            currentProp = (currentProp % inventory.size() + inventory.size()) % inventory.size();
            setTargetPosition();
        }

        updatePropsPosition(dt);

        this.isActive = isActive;
        if (!animator.isConvertationDone())
            if (this.isActive)
                animator.expandShape(dt);

            if (!this.isActive)
                animator.shrinkShape(dt);
    }

    public void render() {
        // batch.end();
        if (isActive) {
            osm.drawRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, new Color(0, 0, 0, 0.4f), true);
            osm.render();

            for (Prop prop : inventory) {
                System.out.println(prop.getPosition().x);
                System.out.println(prop.getPosition().y);
                prop.render(batch);
            }

            batch.begin();

            Prop prop = inventory.get(currentProp);
            font.draw(batch, prop.getName(),
                Constants.WINDOW_WIDTH / 2 - (prop.getName().length() * 24) / 2, Constants.WINDOW_HEIGHT / 2 - Constants.WINDOW_HEIGHT / 6);

            batch.end();
        }
        // batch.begin();
    }
}
