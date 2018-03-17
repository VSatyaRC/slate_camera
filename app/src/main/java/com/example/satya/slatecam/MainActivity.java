package com.example.satya.slatecam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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




    static final int REQUEST_TAKE_PHOTO = 1;
    //
//    public void startCamera(View view) throws IOException{
//        imageFile();
//        startCam();
//        galleryAddPic();
//    }

    public void startCamera(View view) throws IOException {

        Intent capturedPic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capturedPic.resolveActivity(getPackageManager()) != null) {
            File capturedFile = null;
            try {
                capturedFile = imageFile();
            } catch (IOException ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            }
            if (capturedFile != null) {
                capturedPic.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(capturedFile));
                startActivityForResult(capturedPic, REQUEST_TAKE_PHOTO);
            }
        }
    }


    String originalImagePath;

    private File imageFile() throws IOException {
        // Create an image file name
        String crntTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir, "IMG_" + crntTime + ".jpg");
        originalImagePath = image.getAbsolutePath();
        return image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Intent intent = new Intent(this, EditSlate.class);
            intent.putExtra("path", originalImagePath);
            startActivity(intent);
        }
    }


}