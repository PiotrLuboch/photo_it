package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by piotr on 18.12.16.
 */

public class NegativeFilter implements Filter {
    @Override
    public Bitmap filter(Bitmap src) {
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        int index = 0;
        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                index = y * width + x;
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                pixels[index] = Color.argb(A, R, G, B);
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        // return final bitmap
        return bmOut;
    }
}
