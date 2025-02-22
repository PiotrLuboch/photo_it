package com.example.michal.photoit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.michal.photoit.filtering.GrayFilter;
import com.example.michal.photoit.filtering.Moustache;
import com.example.michal.photoit.filtering.NegativeFilter;
import com.example.michal.photoit.filtering.SepiaFilter;
import com.example.michal.photoit.filtering.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayImage extends AppCompatActivity {

    private byte[] bytes;
    ImageView view;

    private String mImageFileName;
    private File mImageFolder;
    Bitmap image;
    List<Bitmap> previous;

    public DisplayImage(){}
    private boolean isMoustache;
    private boolean isText;

    private String text;

    public DisplayImage(byte[] bytes){
        this.bytes = bytes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Bundle b = getIntent().getExtras();
        this.bytes = b.getByteArray("image");
        isMoustache=false;
        isText=false;
        final ArrayList<Bitmap> previous = new ArrayList<Bitmap>(0);

        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image=bitmap;
        view = (ImageView) findViewById(R.id.imageView);
        // NegativeFilter neg = new NegativeFilter();
        //bitmap=neg.filter(bitmap);
        view.setImageBitmap(bitmap);
        ImageButton negative = (ImageButton) findViewById(R.id.negative);
        ImageButton gray = (ImageButton) findViewById(R.id.gray);
        ImageButton sepia = (ImageButton) findViewById(R.id.sepia);
        ImageButton insertText = (ImageButton) findViewById(R.id.insertText);
        final ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        final ImageButton save = (ImageButton) findViewById(R.id.save);
        ImageButton moustache = (ImageButton) findViewById(R.id.moustache);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image=previous.get(previous.size()-1);
                previous.remove(previous.size()-1);
                if (previous.size()==0)
                    back.setVisibility(View.GONE);
                view.setImageBitmap(image);

            }
        });


        insertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isText=true;
                final AlertDialog.Builder alert = new AlertDialog.Builder(DisplayImage.this);

                final EditText edittext = new EditText(getApplicationContext());
                alert.setMessage("Type your text here:");
                alert.setTitle("Text tool");

                alert.setView(edittext);

                alert.setPositiveButton("Set Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        text = edittext.getText().toString();
                        Toast.makeText(DisplayImage.this,text+": Select place.",Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });


        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NegativeFilter neg = new NegativeFilter();
                back.setVisibility(View.VISIBLE);
                previous.add(image);
                image=neg.filter(image);
                view.setImageBitmap(image);

            }
        });
        sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SepiaFilter sep = new SepiaFilter();
                back.setVisibility(View.VISIBLE);
                previous.add(image);
                image=sep.filter(image);
                view.setImageBitmap(image);

            }
        });
        moustache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Moustache moustache = new Moustache(getApplicationContext(), view, bitmap);
                // image=moustache.filter(bitmap);
                //view.setImageBitmap(image);
                isMoustache=true;
                Toast.makeText(getApplicationContext(), "Select place", Toast.LENGTH_SHORT).show();

            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrayFilter gray = new GrayFilter();
                back.setVisibility(View.VISIBLE);
                previous.add(image);
                image=gray.filter(image);
                view.setImageBitmap(image);

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] viewCoords = new int[2];
                view.getLocationOnScreen(viewCoords);

                final int index = event.getActionIndex();
                final float[] cords = new float[]{event.getX(index), event.getY(index)};

                if (isMoustache) {
                    Moustache moustache = new Moustache(getApplicationContext(), view, bitmap, cords);
                    back.setVisibility(View.VISIBLE);
                    previous.add(image);
                    image=moustache.filter(image);
                    isMoustache=false;
                }
                if (isText){

                    Text doText = new Text();

                    back.setVisibility(View.VISIBLE);
                    previous.add(image);
                    image=doText.filter(image, cords, text);
                    isText=false;
                }
                view.setImageBitmap(image);

                return true;
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    save(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void save(Bitmap bitmap) throws IOException {
        FileOutputStream output = null;
        createImageFolder();
        createImageFileName();

        try {
            output = new FileOutputStream(mImageFileName);
            image.compress(Bitmap.CompressFormat.PNG, 100, output); // bmp is your Bitmap instance
            Toast.makeText(getApplicationContext(), "Image saved in:"+mImageFileName, Toast.LENGTH_SHORT).show();

            Intent mediaStoreUpdateIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaStoreUpdateIntent.setData(Uri.fromFile(new File(mImageFileName)));
            sendBroadcast(mediaStoreUpdateIntent);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }

    private File createImageFileName() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prepend = "IMAGE_"+timestamp;

        File imageFile = File.createTempFile(prepend, ".png", mImageFolder);

        mImageFileName = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void createImageFolder(){
        File imageFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mImageFolder = new File(imageFile,"PhotoIt!");

        if (!mImageFolder.exists()){
            mImageFolder.mkdirs();
        }
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
