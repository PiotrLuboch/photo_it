package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Michal on 26.01.2017.
 */

public class GrayFilter implements Filter {
    @Override
    public Bitmap filter(Bitmap src) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        int index=0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                index = y * width + x;
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixels[index]);
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                pixels[index] = Color.argb(A, R, G, B);
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }
}
