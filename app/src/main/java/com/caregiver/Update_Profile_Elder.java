package com.caregiver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.caregiver.Model.Elder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Update_Profile_Elder extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    private String filePath;
    private int MY_CAMERA_REQUEST_CODE =100;
    private ImageView img;
    private Elder elderDB;

    private int angle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile__elder);
        elderDB = (Elder) getIntent().getSerializableExtra("elderDB");
        angle = 90;


    }

    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {


                } else {
                    startActivity(new Intent(Update_Profile_Elder.this,Authentication.class));
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }


    }


    public void selectDisplayBySelfie(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "caregiver_" + timeStamp + ".jpg";
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
        Uri fileUri = Uri.fromFile(f);
        filePath = fileUri.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, 0);

    }

    public void selectDisplayByGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "care_giver_" + timeStamp + ".jpg";
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
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 400, 400, true);
                Matrix mat = new Matrix();
                mat.postRotate(270);
                bmpPic = Bitmap.createBitmap(bmpPic, 0, 0, bmpPic.getWidth(), bmpPic.getHeight(), mat, true);
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
                bmpPic = Bitmap.createScaledBitmap(bmpPic, 400, 400, true);
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
        StorageReference imageRef = storageRef.child("display_elder/"+elderDB.getUid()+".png");

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
                elderDB.setPhoto_path(taskSnapshot.getDownloadUrl().toString());
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mUsersRef = mRootRef.child("Elder");
                mUsersRef.child(elderDB.getUid()).setValue(elderDB);
                Intent intent = new Intent(Update_Profile_Elder.this,Map_Show.class);
                intent.putExtra("elderDB",elderDB);
                startActivity(intent);

                Log.d("success", "onSuccess: ");
            }
        });
    }

    public void onClickRotateDisplay(View view){
        try {
            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
            img.setImageBitmap(bitmap);
        }catch (Exception e){

        }

    }

}
