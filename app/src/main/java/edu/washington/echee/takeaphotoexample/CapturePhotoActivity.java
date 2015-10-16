package edu.washington.echee.takeaphotoexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapturePhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 123;
    ImageView mImageView;
    String mCurrentPhotoPath = ""; // the path of the last image file created

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        this.mImageView = (ImageView) findViewById(R.id.ivPreview);

        // Pressing "Take a Pic" will fire up the Camera app
        Button btnTakeAPicture = (Button) findViewById(R.id.btnTakeAPicture);
        btnTakeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPicture();
            }
        });

        Log.i("MainActivity", getFilesDir().getAbsolutePath());

    }


    // This creates a temporary file and dispatches the camera to take a photo
    private void takeAPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure there's a camera activity to handle intent
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // create a temp file where the photo will go
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("MainActivity", "Cannot create file");
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));   // tell where the camera should save the photo
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);   // fire Camera app to request an image capture
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create file name with time stamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";

        // Grab storage directory of Pictures/
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        // Create temporary file with the name , file type, & path
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();  // this is a URI string
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // uncomment if you want to get the thumbnail
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
            mImageView.setImageURI(Uri.parse(mCurrentPhotoPath));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
