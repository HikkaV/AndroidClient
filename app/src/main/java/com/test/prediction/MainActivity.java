package com.test.prediction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    private final String path = Environment.getExternalStorageDirectory().getPath();
    File file = new File(path);
    Button sendButton;
    byte[] dataarr ;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button camButton = findViewById(R.id.btnCamera);
        imageView = findViewById(R.id.imageView);
        sendButton = findViewById(R.id.send_but);
        TextView textView = findViewById(R.id.Show);
        sendButton.setEnabled(false);

        camButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);

        });
        sendButton.setOnClickListener(v -> {


            Client client = new Client( "34.73.106.119", 80 , textView, dataarr );
            client.execute();
            sendButton.setEnabled(false);
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        Bitmap bitmap =  (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        imageView.setImageBitmap(bitmap);
        sendButton.setBackgroundColor(Color.GREEN);
        sendButton.setEnabled(true);


        try {    ByteArrayOutputStream baos = new ByteArrayOutputStream();
           Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataarr = baos.toByteArray();
            String s = dataarr.length + "";
            Log.i("length",s );


        }catch (Exception e){
            e.printStackTrace();
            Log.e(null, "Save File Error");
        }

    }

}