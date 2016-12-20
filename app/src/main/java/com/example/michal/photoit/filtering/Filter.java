package com.example.michal.photoit.filtering;

import android.graphics.Bitmap;

/**
 * Created by piotr on 18.12.16.
 */

public interface Filter {
    Bitmap filter(Bitmap src);
    byte[] filter(byte[] src);
}
