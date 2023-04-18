package org.knalpot.knalpot.addons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {
    
    //#region -- VARIABLES --
    private float FRACTION_1;
    private float FRACTION_2;
    private float DELTA_FR;

    private Texture texture;
    private OrthographicCamera camera;
    //#endregion

    //#region -- GENERAL FUNC --
    public ParallaxLayer(Texture texture, OrthographicCamera camera, float fractOne, float fractTwo) {
        this.texture = texture;
        this.camera = camera;
        this.FRACTION_1 = fractOne;
        this.FRACTION_2 = fractTwo;
        DELTA_FR = FRACTION_2 - FRACTION_1;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
    }

    public void render(SpriteBatch batch, float targetX, float targetY) {
        float cameraX = camera.position.x - camera.viewportWidth / 2;
        float cameraY = camera.position.y - camera.viewportHeight / 2;

        // NB! THIS IS A TEMPORARY SOLUTION, IT MIGHT LEAD TO EITHER
        // MEMORY LEAK OR MEMORY OVERLOAD. COEFFICENT MUST BE
        // FOUND MORE EFFICIENTLY.
        float x = cameraX * FRACTION_2 + targetX * FRACTION_1;
        double randomFormula = (camera.position.x / camera.viewportWidth) * (camera.viewportWidth / texture.getWidth()) * (1.0 - Math.abs(DELTA_FR));
        float cameraTextureCoefficent = camera.viewportWidth / texture.getWidth();
        int timesToRepeat = (int) Math.round(randomFormula / cameraTextureCoefficent) + 1;

        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(0);
        region.setRegionY(0);
        region.setRegionWidth(texture.getWidth() * timesToRepeat);
        region.setRegionHeight(texture.getHeight());

        batch.draw(region, x, 100, camera.viewportWidth * timesToRepeat, camera.viewportHeight);
    }
    //#endregion
}
