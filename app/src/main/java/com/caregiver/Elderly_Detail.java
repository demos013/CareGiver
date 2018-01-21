package com.caregiver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Elder;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import io.nlopez.smartlocation.SmartLocation;

public class Elderly_Detail extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Elder elderDB;
    Request_Care_Activity request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly__detail);
        elderDB = (Elder) getIntent().getSerializableExtra("elderDB");
        ImageView img = findViewById(R.id.elderly_detail_display);
        downloadInLocalFile(img,elderDB);


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
                    //setupNavigationBar();
                    getRequestActivity();



                } else {
                    startActivity(new Intent(Elderly_Detail.this,Authentication.class));
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

        SmartLocation.with(this)
                .location()
                .stop();
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

    private void downloadInLocalFile(final ImageView img, Elder elderDB) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("display_elder/"+elderDB.getUid()+".png");
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

    public void onCallElder(View view){
        String phone = elderDB.getMobile_number();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);

    }

    public void onConfirmActivity(View view){
        showDialogConfirmActivity(view);

    }

    public void showDialogConfirmActivity(final View view){

        final Dialog dialog = new Dialog(Elderly_Detail.this);
        dialog.setContentView(R.layout.dialog_confirm_activity);

        final EditText confirm_key = (EditText) dialog.findViewById(R.id.dialog_confirm_activity_key);

        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_confirm_activity_cancel);
        Button buttonAdd = (Button) dialog.findViewById(R.id.dialog_confirm_activity_ok);
        final EditText dateedt = dialog.findViewById(R.id.dialog_confirm_activity_date);
        dateedt.setInputType(InputType.TYPE_NULL);
        dateedt.setTextIsSelectable(true);
        final EditText timeedt = dialog.findViewById(R.id.dialog_confirm_activity_time);
        timeedt.setInputType(InputType.TYPE_NULL);
        timeedt.setTextIsSelectable(true);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirm_key.getText().toString().equals(String.valueOf(request.getConfirm_key()))){
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mCareActivity = mRootRef.child("Care_Activity");
                    Care_Activity activity = new Care_Activity();
                    activity.setCaregiver_uid(user.getUid());
                    activity.setElder_uid(elderDB.getUid());
                    activity.setConfirm_key(String.valueOf(request.getConfirm_key()));
                    activity.setStart_date(dateedt.getText().toString());
                    activity.setStart_time(timeedt.getText().toString());
                    activity.setStart_key(generateRandomNumber());
                    mCareActivity.child(elderDB.getUid()+user.getUid()).setValue(activity);

                    DatabaseReference mRequestActivity = mRootRef.child("Request_Care_Activity");
                    mRequestActivity.child(elderDB.getUid()+user.getUid()).removeValue();
                    dialog.dismiss();

                }
                else{
                    confirm_key.setError("Invalid confirm key!!!");
                }
            }
        });

        //date picker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateedt.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dateedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Elderly_Detail.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }


        });
        //time picker
        timeedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Elderly_Detail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeedt.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }
        });


        dialog.show();

    }

    public void getRequestActivity(){
        request = new Request_Care_Activity();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Request_Care_Activity");
        Query query = mUsersRef.orderByKey().equalTo(elderDB.getUid()+user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    request = postSnapshot.getValue(Request_Care_Activity.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public String generateRandomNumber() {

        SecureRandom secureRandom = new SecureRandom();
        String randomNumber = "";
        for (int i = 0; i < 10; i++) {
            int number = secureRandom.nextInt(9);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            randomNumber = randomNumber + number;
        }
        return randomNumber;
    }


}
