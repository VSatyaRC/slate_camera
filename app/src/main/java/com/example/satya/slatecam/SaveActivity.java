package com.example.satya.slatecam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        displayView();

    }


    public void displayView() {

        Intent intent = getIntent();
        String path = intent.getStringExtra("pathToSource");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView imageView = (ImageView) findViewById(R.id.src_img);
        imageView.setImageBitmap(bitmap);
        String text = intent.getStringExtra(EditSlate.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.show_text);
        Typeface goodFootTypeFace = Typeface.createFromAsset(getAssets(),"fonts/goodfoot.ttf");
        textView.setTypeface(goodFootTypeFace);
        textView.setText(text);

    }

    public void savePic(View view) {

        Intent save_pic = getIntent();
        String isSaveTrue = save_pic.getStringExtra("savePic");
        int save = Integer.parseInt(isSaveTrue);

        if (save == 1) {
            galleryAddPic();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                finish();

            } else
                Toast.makeText(this, "No Receiver", Toast.LENGTH_LONG).show();
            finish();

        } else {
            finish();
        }
    }


    public Bitmap saveView() {

        final View view = findViewById(R.id.layout);
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    String finalImagePath;

    private void saveBitmap(Bitmap bitmap) {
        String crntTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dirToStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        OutputStream fileOut = null;


        File image = new File(dirToStore, "EDITED_IMG_" + crntTime + ".jpg");
        try {

            fileOut = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        finalImagePath = image.getAbsolutePath();

    }

    Uri uri;

    private void galleryAddPic() {
        saveBitmap(saveView());
        File file = new File(finalImagePath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "Image Saved to Pictures", Toast.LENGTH_SHORT).show();
    }

}

