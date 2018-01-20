package com.caregiver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Request_Care_Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

import io.nlopez.smartlocation.SmartLocation;

public class Caregiver_Detail extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private Caregiver caregiverDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver__detail);
        caregiverDB = (Caregiver) getIntent().getSerializableExtra("caregiverDB");
        TextView tmp = findViewById(R.id.caregiver_detail_name);
        tmp.setText(caregiverDB.getName()+" "+caregiverDB.getLastname());
        ImageView img = findViewById(R.id.caregiver_detail_display);
        downloadInLocalFile(img,caregiverDB);


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //getUserDB(user.getUid());
                    //setupNavigationBar();
                    //updateLocation();
                    //getCaregiverDB();
                    isBooking();



                } else {
                    startActivity(new Intent(Caregiver_Detail.this,Authentication.class));
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

        /*SmartLocation.with(this)
                .location()
                .stop();*/
    }

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }

    private void downloadInLocalFile(final ImageView img, Caregiver caregiverDB) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display_caregiver/"+caregiverDB.getUid()+".png");
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        final File file = new File(dir, UUID.randomUUID().toString() + ".png");
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final FileDownloadTask fileDownloadTask = imageRef.getFile(file);


        fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmpPic = BitmapFactory.decodeFile(file.getPath());
                img.setImageBitmap( GetBitmapClippedCircle(bmpPic));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    public void onBookingCaregiver (View view){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Request_Care_Activity");
        Request_Care_Activity request = new Request_Care_Activity();
        int confirmkey = generateRandomNumber();
        request.setCaregiver_id(caregiverDB.getUid());
        request.setElder_uid(user.getUid());
        request.setConfirm_key(confirmkey);
        mUsersRef.child(user.getUid()+caregiverDB.getUid()).setValue(request);
        Button bookingbt = findViewById(R.id.caregiver_detail_booking_button);
        bookingbt.setVisibility(View.GONE);
        TextView confirmkeytxt = findViewById(R.id.caregiver_detail_confirm_key);
        confirmkeytxt.setText(String.valueOf(confirmkey));
        confirmkeytxt.setVisibility(View.VISIBLE);



    }

    public int generateRandomNumber() {
        int randomNumber;
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < 6; i++) {
            int number = secureRandom.nextInt(9);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        randomNumber = Integer.parseInt(s);
        return randomNumber;
    }

    public void isBooking(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRequestRef = mRootRef.child("Request_Care_Activity");
        Query query =  mRequestRef.orderByKey().equalTo(user.getUid()+caregiverDB.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Request_Care_Activity request = postSnapshot.getValue(Request_Care_Activity.class);

                    Button bookingbt = findViewById(R.id.caregiver_detail_booking_button);
                    bookingbt.setVisibility(View.GONE);
                    TextView confirmkeytxt = findViewById(R.id.caregiver_detail_confirm_key);
                    confirmkeytxt.setText(String.valueOf(request.getConfirm_key()));
                    confirmkeytxt.setVisibility(View.VISIBLE);
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
