package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by piotr on 18.12.16.
 */

public final class SepiaFilter implements Filter {
    @Override
    public Bitmap filter(Bitmap src) {
        int depth = 10;
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                int avg = (Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) / 3;
                R = avg + 2 * depth;
                G = avg + depth;
                B = depth;
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;
    }
}
