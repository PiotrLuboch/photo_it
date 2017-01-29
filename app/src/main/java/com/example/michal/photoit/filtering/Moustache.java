package com.example.michal.photoit.filtering;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.renderscript.Allocation;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.michal.photoit.DisplayImage;
import com.example.michal.photoit.PhotoCamera;
import com.example.michal.photoit.R;
import com.example.michal.photoit.filtering.Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michal on 28.01.2017.
 */

public class Moustache implements Filter {

    Context context;
    ImageView view;
    Bitmap source;
    boolean exit;
    public Moustache(){};
    public Moustache(Context context, ImageView view, Bitmap source){
        this.context = context;
        this.view=view;
        this.source=source;

    }

    public Bitmap filter(Bitmap src) {
        this.source=src;
        //view.setBackground(new BitmapDrawable(context.getResources(), src));
        final Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moustache);

        //view.setImageBitmap(bmp2);

        Bitmap bmOverlay = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(src, new Matrix(), null);
        canvas.drawBitmap(bmp2, (src.getWidth()/2)- bmp2.getWidth()/2, (src.getHeight()/2)-bmp2.getHeight()/2, null);
        return bmOverlay;
    }

}
