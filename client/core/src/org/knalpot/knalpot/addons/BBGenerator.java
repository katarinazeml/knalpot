package org.knalpot.knalpot.addons;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;

/**
 * {@code BBGenerator} class generated a bounding box for the specified
 * texture. It is mainly used as an addon to {@link org.knalpot.knalpot.actors.Actor Actor}.
 * Mainly utilizes privately created classes for calculating width and height of BB based
 * on byte-size of pixel in the image.
 * @author Max Usmanov
 * @version 0.1
 */
public class BBGenerator {

    /**
     * Calculates and returns a width and height of a texture.
     * @param data
     * @return int[]
     */
    public static int[] BBPixels(TextureData data) {
        int[] size = new int[2];

        if (!data.isPrepared()) data.prepare();
        Pixmap image = data.consumePixmap();

        size[0] = countWidth(image);
        size[1] = countHeight(image);
        image.dispose();
        return size;
    }

    private static int countWidth(Pixmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] notAlpha = new int[height];

        for (int y = 0; y < height; y++) {
            int temporary = 0;
            for (int x = 0; x < width; x++) {
                int pixel = image.getPixel(x, y);
                if (pixel != 0) {
                    temporary++;
                }
            }
            notAlpha[y] = temporary;
        }
        return Arrays.stream(notAlpha).summaryStatistics().getMax();
    }

    private static int countHeight(Pixmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] notAlpha = new int[width];

        for (int x = 0; x < width; x++) {
            int temporary = 0;
            for (int y = 0; y < height; y++) {
                int pixel = image.getPixel(x, y);
                if (pixel != 0) {
                    temporary++;
                }
            }
            notAlpha[x] = temporary;
        }
        return Arrays.stream(notAlpha).summaryStatistics().getMax();
    }
}
