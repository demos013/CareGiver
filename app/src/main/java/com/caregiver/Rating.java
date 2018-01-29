package com.caregiver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Elder;
import com.caregiver.Model.Record_Care_Activity;
import com.caregiver.Model.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Rating extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Care_Activity activity;
    Caregiver caregiverDB;
    Review rating = new Review();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
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
                    activity = (Care_Activity) getIntent().getSerializableExtra("activity");
                    getCaregiverDB();
                    setRating();
                    rating = new Review();
                    //setupNavigationBar();
                    //updateLocation();
                    //getCaregiverDB();
                } else {
                    startActivity(new Intent(Rating.this,Authentication.class));
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
                file.delete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    public void getCaregiverDB(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mCaregiverRef = mRootRef.child("Caregiver").child(activity.getCaregiver_uid());
        mCaregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                caregiverDB = dataSnapshot.getValue(Caregiver.class);
                updateCaregiver();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateCaregiver(){
        ImageView img = findViewById(R.id.rating_display);
       TextView name = findViewById(R.id.rating_name);
        downloadInLocalFile(img,caregiverDB);
        name.setText("คุณ "+caregiverDB.getName()+" "+caregiverDB.getLastname());
    }
    public void setRating(){
        RatingBar ratingBar = findViewById(R.id.rating_rating_bar);
        ratingBar.setRating(0);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
                rating.setScore(v);
            }
        });
    }

    public void sendOpinion(View view){

        EditText opinion = findViewById(R.id.rating_opinion);
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        rating.setElder_uid(user.getUid());
        rating.setCaregiver_uid(activity.getCaregiver_uid());
        rating.setReview_detail(opinion.getText().toString());
        rating.setDate_of_view(sdf.format(myCalendar.getTime()));



        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRatingRef = mRootRef.child("Review");
        mRatingRef.child(user.getUid()+activity.getCaregiver_uid()).setValue(rating);

        DatabaseReference mRecordActivityRef = mRootRef.child("Record_Care_Activity");
        Record_Care_Activity record = new Record_Care_Activity();
        record.setActivity_detail(activity.getActivity_detail());
        record.setCaregiver_uid(activity.getCaregiver_uid());
        record.setElder_uid(activity.getElder_uid());
        record.setFinish_date(activity.getFinish_date());
        record.setFinish_time(activity.getFinish_time());
        record.setStart_date(activity.getStart_date());
        record.setStart_time(activity.getStart_time());
        mRecordActivityRef.push().setValue(record);





        DatabaseReference mActivityRef = mRootRef.child("Care_Activity");
        mActivityRef.child(activity.getElder_uid()+activity.getCaregiver_uid()).removeValue();

        startActivity(new Intent(Rating.this,Map_Show.class));
    }
}
