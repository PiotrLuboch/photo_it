package com.example.michal.photoit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michal.photoit.filtering.GrayFilter;
import com.example.michal.photoit.filtering.NegativeFilter;
import com.example.michal.photoit.filtering.SepiaFilter;

public class DisplayImage extends AppCompatActivity {

    private byte[] bytes;
    ImageView view;

    public DisplayImage(){}

    public DisplayImage(byte[] bytes){
        this.bytes = bytes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Bundle b = getIntent().getExtras();
        this.bytes = b.getByteArray("image");

        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
         view = (ImageView) findViewById(R.id.imageView);
       // NegativeFilter neg = new NegativeFilter();
        //bitmap=neg.filter(bitmap);
        view.setImageBitmap(bitmap);
        ImageButton negative = (ImageButton) findViewById(R.id.negative);
        ImageButton gray = (ImageButton) findViewById(R.id.gray);
        ImageButton sepia = (ImageButton) findViewById(R.id.sepia);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NegativeFilter neg = new NegativeFilter();
                view.setImageBitmap(neg.filter(bitmap));

            }
        });
        sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SepiaFilter sep = new SepiaFilter();
                view.setImageBitmap(sep.filter(bitmap));

            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrayFilter gray = new GrayFilter();
                view.setImageBitmap(gray.filter(bitmap));

            }
        });

    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        if (hasFocus){
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}
