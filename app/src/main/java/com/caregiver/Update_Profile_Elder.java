package com.caregiver;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Update_Profile_Elder extends AppCompatActivity {

    private String filePath;
    private int MY_CAMERA_REQUEST_CODE =100;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__elder);

        // Permission StrictMode
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_CAMERA_REQUEST_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
    }


    public void selectDisplayBySelfie(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "pkl_" + timeStamp + ".jpg";
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
        Uri fileUri = Uri.fromFile(f);
        filePath = fileUri.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, 0);

    }

    public void selectDisplayByGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "pkl_" + timeStamp + ".jpg";
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
        Uri fileUri = Uri.fromFile(f);
        filePath = fileUri.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== 0 && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bmpPic = BitmapFactory.decodeFile(filePath.replace("file://", ""));
                FileOutputStream bmpFile = new FileOutputStream(filePath.replace("file://", ""));
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 600, 400, true);
                Matrix mat = new Matrix();
                mat.postRotate(270);
                bmpPic = Bitmap.createBitmap(bmpPic, 0, 0, bmpPic.getWidth(), bmpPic.getHeight(), mat, true);
                bmpPic.compress(Bitmap.CompressFormat.JPEG, 10, bmpFile);
                bmpFile.flush();
                bmpFile.close();
                img = (ImageView) findViewById(R.id.update_profile_elder_display);
                img.setImageBitmap(bmpPic);
            } catch (Exception e) {
                Log.e("Log", "Error on saving file");
            }
        }

        if(requestCode== 2 && resultCode == Activity.RESULT_OK){
            try{
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Log.d("imgDecodableString",imgDecodableString);
                filePath ="file://"+imgDecodableString;
                Bitmap bmpPic = BitmapFactory.decodeFile(imgDecodableString);
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 400, 600, true);
                img = (ImageView) findViewById(R.id.update_profile_elder_display);
                img.setImageBitmap(bmpPic);
            }catch  (Exception e) {
                Log.e("Log", "Error on saving file");
            }
        }
    }

    public void updateDisplay(View view){

        // Get the data from an ImageView as bytes
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display/firebase.png");

        UploadTask mUploadTask = imageRef.putBytes(data);
        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("failed", "onFailure: ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d("success", "onSuccess: ");
            }
        });
    }

}
