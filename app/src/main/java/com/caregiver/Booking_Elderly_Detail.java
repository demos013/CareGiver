package com.caregiver;

import android.app.Dialog;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.MotionEvent;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Elder;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


import io.nlopez.smartlocation.SmartLocation;

public class Booking_Elderly_Detail extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Elder elderDB;
    Care_Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__elderly__detail);

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
                    //setupNavigationBar();
                    if(activity.getStart_check()){
                        EditText dailyedt = findViewById(R.id.booking_elderly_detail_daily);
                        dailyedt.setEnabled(true);

                        Button tmp = findViewById(R.id.booking_elderly_detail_end_button);
                        if(activity.getFinish_check()){
                            tmp.setVisibility(View.GONE);
                        }
                        tmp.setVisibility(View.VISIBLE);
                        tmp = findViewById(R.id.booking_elderly_detail_start_button);
                        tmp.setVisibility(View.GONE);
                    }
                    getElderDB();
                    setDailyUpdate();




                } else {
                    startActivity(new Intent(Booking_Elderly_Detail.this,Authentication.class));
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
                file.delete();

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

    public void getElderDB(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Elder");
        mUsersRef.child(activity.getElder_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elderDB = dataSnapshot.getValue(Elder.class);
                ImageView img = findViewById(R.id.booking_elderly_detail_display);
                downloadInLocalFile(img,elderDB);
                TextView nametxt = findViewById(R.id.booking_elderly_datail_name);
                TextView agetxt = findViewById(R.id.booking_elderly_detail_age);
                TextView sextxt = findViewById(R.id.booking_elderly_detail_sex);
                TextView bloodtxt = findViewById(R.id.booking_elderly_detail_blood);
                TextView jobtxt = findViewById(R.id.booking_elderly_detail_job);
                TextView drugtxt = findViewById(R.id.booking_elderly_detail_drug_allergy);
                EditText dailyedt = findViewById(R.id.booking_elderly_detail_daily);
                nametxt.setText("คุณ "+elderDB.getName()+" "+elderDB.getLastname());
                String tmp = elderDB.getDate_of_birth();
                String[] split = tmp.split("/");
                int age = Calendar.getInstance().get(Calendar.YEAR)-Integer.valueOf(split[2]);
                agetxt.setText("อายุ "+String.valueOf(age)+" ปี");
                sextxt.setText("เพศ "+elderDB.getSex());
                bloodtxt.setText("หมู่เลือด "+elderDB.getBlood_group());
                jobtxt.setText("อาชีพ "+elderDB.getJob());
                drugtxt.setText("ยาที่แพ้ "+elderDB.getDrug_allergy());
                dailyedt.setText(activity.getActivity_detail());
                /*datetxt.append(request.getStart_date());
                timetxt.append(request.getStart_time()+" นาฬิกา");*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void setDailyUpdate(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mActivityRef = mRootRef.child("Care_Activity").child(activity.getElder_uid()+user.getUid());
        final Map<String,Object> activityDetail = new HashMap<>();
        final EditText dailyedt = findViewById(R.id.booking_elderly_detail_daily);
        dailyedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activityDetail.put("activity_detail",charSequence.toString());
                mActivityRef.updateChildren(activityDetail);
                //dailyedt.setSelection(dailyedt.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void onStartActivity(View view){
            Log.d("ok", "onActivityResult: ");
            Intent intentOption = new Intent(Booking_Elderly_Detail.this,QRCodeScaner.class);
            intentOption.putExtra("activity",activity);
            startActivity(intentOption);
    }

    public  void onEndActivity(View view){
        showDialogConfirmActivity();


    }

    public void showDialogConfirmActivity(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_activity);

        Button ok = dialog.findViewById(R.id.dialog_finish_activity_ok);
        Button cancel = dialog.findViewById(R.id.dialog_finish_activity_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mActivityRef = mRootRef.child("Care_Activity").child(activity.getElder_uid()+user.getUid());
                Map<String,Object> activity_finish_check = new HashMap<>();
                activity_finish_check.put("finish_check",true);
                Map<String,Object> activity_finish_date = new HashMap<>();
                Map<String,Object> activity_finish_time = new HashMap<>();

                Calendar myCalendar = Calendar.getInstance();
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                activity_finish_date.put("finish_date",sdf.format(myCalendar.getTime()));

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                myFormat = "HH:mm";
                sdf = new SimpleDateFormat(myFormat,Locale.US);
                activity_finish_time.put("finish_time",sdf.format(myCalendar.getTime()));


                mActivityRef.updateChildren(activity_finish_check);
                mActivityRef.updateChildren(activity_finish_date);
                mActivityRef.updateChildren(activity_finish_time);
                dialog.dismiss();
                Button finishButton = findViewById(R.id.booking_elderly_detail_end_button);
                finishButton.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
