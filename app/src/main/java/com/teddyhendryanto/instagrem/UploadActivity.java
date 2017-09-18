package com.teddyhendryanto.instagrem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity {

    Button btn_upload_gallery;
    Button btn_upload_camera;
    Button btn_upload_to_server;

    ImageView image_view;
    EditText tv_caption;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btn_upload_gallery = (Button) findViewById(R.id.btn_upload_gallery);
        btn_upload_camera = (Button) findViewById(R.id.btn_upload_camera);
        btn_upload_to_server = (Button) findViewById(R.id.btn_upload_to_server);
        image_view = (ImageView) findViewById(R.id.image_view);
        tv_caption = (EditText) findViewById(R.id.tv_caption);

        btn_upload_gallery.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
                    }
                    else{
                        getGallery();
                    }
                }
                else{
                    getGallery();
                }
            }
        });

        btn_upload_camera.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // check permission dulu untuk akses kamera android M keatas
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                    }
                    else{
                        // sudah permission granted
                        getCamera();
                    }
                }
                else{
                    // android dibawah M, ga perlu cek permission
                    getCamera();
                }
            }
        });

        btn_upload_to_server.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap == null){
                    Toast.makeText(UploadActivity.this, "Gambar Kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);

                ParseObject obj = new ParseObject("Pictures");
                obj.put("username", ParseUser.getCurrentUser().getUsername());
                obj.put("image", file);
                obj.put("caption", tv_caption.getText().toString());
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(UploadActivity.this, "Upload Berhasil.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UploadActivity.this, "Upload Gagal. Error =" +e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCamera();
            }
        }
        else if(requestCode == REQUEST_IMAGE_PICK){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getGallery();
            }
        }
    }

    private void getGallery() {
        Intent getGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(getGalleryIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(getGalleryIntent, REQUEST_IMAGE_PICK);
        }
    }

    private void getCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // resolveActivity untuk cek tujuan intent ny ada atau tidak. kl tidak = null berrti tujuannya ada
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // startActivityForResult harus di overide krn minta hasil tujuannya ==> jalanin onActivityResult
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // request code cek apakah permission allow camera
        // result code cek apakah kamera ny di take picture atau di back
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if (resultCode == RESULT_OK && data != null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmap = imageBitmap;
                image_view.setImageBitmap(imageBitmap);
            }
            else{
                Toast.makeText(this, "Tidak jadi ambil gambar.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_IMAGE_PICK){
            if (resultCode == RESULT_OK && data != null){
                Uri selectedImage = data.getData();

                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    bitmap = imageBitmap;
                    image_view.setImageBitmap(imageBitmap);
                }
                catch (Exception e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Tidak jadi buka gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
