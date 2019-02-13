package com.test.prediction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    private final String path = Environment.getExternalStorageDirectory().getPath();
    File file = new File(path);
    Button sendButton;
    byte[] dataarr ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button camButton = (Button) findViewById(R.id.btnCamera);
        imageView = (ImageView) findViewById(R.id.imageView);
        sendButton = (Button) findViewById(R.id.send_but);
        TextView textView = findViewById(R.id.Show);
        sendButton.setEnabled(false);

        camButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);

        });
        sendButton.setOnClickListener(v -> {

            Client client = new Client( "192.168.1.106", 8888 , textView, dataarr );
            client.execute();
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap =  (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        sendButton.setBackgroundColor(Color.GREEN);
        sendButton.setEnabled(true);


        try {     ByteArrayOutputStream baos = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataarr = baos.toByteArray();
            String s = dataarr.length + "";
            Log.i("length",s );
//            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//            String pictureFile = "ZOFTINO_" + timeStamp;
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
//            image.createNewFile();
//            Log.i("s", path);


        }catch (Exception e){
            e.printStackTrace();
            Log.e(null, "Save File Error");
        }

    }

}