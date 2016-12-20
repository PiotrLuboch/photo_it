package com.example.michal.photoit.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by piotr on 18.12.16.
 */

public class ImageOperations {
    public Bitmap imageToBitmap(Image image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    public byte[] bitmapToBytes(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] byteArray = byteBuffer.array();
        return byteArray;
    }
}
