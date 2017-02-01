package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.michal.photoit.R;

/**
 * Created by Michal on 01.02.2017.
 */

public class Text {


    public Bitmap filter(Bitmap src,float[] cords, String text) {


        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        Canvas c = new Canvas(bmOut);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        c.drawText(text,cords[0],cords[1],paint);
        return bmOut;

    }
}
