package com.example.satya.slatecam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditSlate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_slate);
        setView(compressImage());

    }

    public final static String EXTRA_MESSAGE = "com.example.satya.slateCam.MESSAGE";

    String editedPhotoPath,imageName;
    Bitmap cmpBitmap;
    public Bitmap compressImage() {

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int width = (int) (bitmap.getWidth() * 0.25f);
        int height = (int) (bitmap.getHeight() * 0.25f);

        cmpBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return cmpBitmap;
    }

    private void saveBitmap(Bitmap bitmap){
        String crntTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dirToStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        OutputStream fileOut = null;
        imageName = "IMG_" + crntTime + ".jpg";

        File image = new File(dirToStore, imageName);
        try {

            fileOut = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOut);
            fileOut.flush();
            fileOut.close();
        }catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        editedPhotoPath=image.getAbsolutePath();
    }
    private void addToGallery() {
        saveBitmap(cmpBitmap);
        File file = new File(editedPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void setView(Bitmap bitmap) {
        ImageView imageView;
        imageView = (ImageView) findViewById(R.id.src_img);
        imageView.setImageBitmap(bitmap);
        addToGallery();

    }

    public void saveView(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        EditText editText = (EditText) findViewById(R.id.edited_text);
        String text = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, text);
        intent.putExtra("pathToSource", editedPhotoPath);
        intent.putExtra("savePic", "1");
        startActivity(intent);
        Toast.makeText(this, "Touch anywhere to SAVE", Toast.LENGTH_LONG).show();
        finish();
    }


    public void preview(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        EditText editText = (EditText) findViewById(R.id.edited_text);
        String text = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, text);
        intent.putExtra("savePic", "0");

        intent.putExtra("pathToSource",editedPhotoPath);
        startActivity(intent);
        Toast.makeText(this, "Touch anywhere to edit", Toast.LENGTH_LONG).show();

    }

}

