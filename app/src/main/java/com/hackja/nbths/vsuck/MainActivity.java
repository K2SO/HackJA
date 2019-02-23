package com.hackja.nbths.vsuck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static File lastPhoto = null;

    public void OpenCam(View view) {
        ImageView img = findViewById(R.id.thumbnail);
        TextView descriptor = findViewById(R.id.your_thumbnail);
        img.setVisibility(View.INVISIBLE);
        descriptor.setVisibility(View.INVISIBLE);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp;
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                lastPhoto = image;
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.hackja.nbths.vsuck.fileprovider",
                        image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException io) {}
        } else Toast.makeText(getApplicationContext(),"Couldn't find a camera app",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(lastPhoto.getAbsolutePath());
            ImageView img = findViewById(R.id.thumbnail);
            img.setImageBitmap(imageBitmap);
            TextView descriptor = findViewById(R.id.your_thumbnail);
            descriptor.setText("We're having trouble identifying this image");

            img.setVisibility(View.VISIBLE);
            descriptor.setVisibility(View.VISIBLE);

        }
    }
}
