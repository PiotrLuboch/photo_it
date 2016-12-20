package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;

/**
 * Created by piotr on 18.12.16.
 */

public final class SepiaFilter implements Filter {
    @Override
    public byte[] filter(byte[] src) {
        return new byte[0];
    }

    @Override
    public Bitmap filter(Bitmap src) {
        return  null;
    }
}
