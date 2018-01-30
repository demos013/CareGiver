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
import android.widget.TextView;
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
        request = (Request_Care_Activity) getIntent().getSerializableExtra("request");

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
                    getElderDB();



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

    public void onConfirmActivity(View view){
        showDialogConfirmActivity(view);

    }

    public void showDialogConfirmActivity(final View view){

        final Dialog dialog = new Dialog(Elderly_Detail.this);
        dialog.setContentView(R.layout.dialog_confirm_activity);

        final EditText confirm_key = (EditText) dialog.findViewById(R.id.dialog_confirm_activity_key);

        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_confirm_activity_cancel);
        Button buttonAdd = (Button) dialog.findViewById(R.id.dialog_confirm_activity_ok);

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
                    activity.setStart_date(request.getStart_date());
                    activity.setStart_time(request.getStart_time());
                    activity.setStart_key(generateRandomNumber());
                    mCareActivity.child(elderDB.getUid()+user.getUid()).setValue(activity);
                    DatabaseReference mRequestActivity = mRootRef.child("Request_Care_Activity");
                    mRequestActivity.child(elderDB.getUid()+user.getUid()).removeValue();
                    dialog.dismiss();
                    Intent intent = new Intent(Elderly_Detail.this, CareGiver_in_box.class);
                    startActivity(intent);

                }
                else{
                    confirm_key.setError("Invalid confirm key!!!");
                }
            }
        });

        dialog.show();

    }

    public void onClickDeleteActivity(final View view){

        final Dialog dialog = new Dialog(Elderly_Detail.this);
        dialog.setContentView(R.layout.dialog_finish_activity);

        TextView txt = dialog.findViewById(R.id.dialog_finish_txt);
        txt.setText("ยกเลิกคำร้องกิจกรรม");

        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_finish_activity_cancel);
        Button buttonOK = (Button) dialog.findViewById(R.id.dialog_finish_activity_ok);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mRequestRef = mRootRef.child("Request_Care_Activity").child(elderDB.getUid()+user.getUid());
                mRequestRef.removeValue();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void getElderDB(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Elder");
        mUsersRef.child(request.getElder_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elderDB = dataSnapshot.getValue(Elder.class);
                ImageView img = findViewById(R.id.elderly_detail_display);
                downloadInLocalFile(img,elderDB);
                TextView nametxt = findViewById(R.id.elderly_datail_name);
                TextView agetxt = findViewById(R.id.elderly_detail_age);
                TextView sextxt = findViewById(R.id.elderly_detail_sex);
                TextView bloodtxt = findViewById(R.id.elderly_detail_blood);
                TextView jobtxt = findViewById(R.id.elderly_detail_job);
                TextView drugtxt = findViewById(R.id.elderly_detail_drug_allergy);
                TextView datetxt = findViewById(R.id.elderly_detail_date);
                TextView timetxt = findViewById(R.id.elderly_detail_time);
                nametxt.setText("คุณ "+elderDB.getName()+" "+elderDB.getLastname());
                String tmp = elderDB.getDate_of_birth();
                Log.d(elderDB.getDate_of_birth(), "onDataChange: ");
                String[] split = tmp.split("/");
                int age = Calendar.getInstance().get(Calendar.YEAR)-Integer.valueOf(split[2]);
                agetxt.setText("อายุ "+String.valueOf(age)+" ปี");
                sextxt.setText("เพศ "+elderDB.getSex());
                bloodtxt.setText("หมู่เลือด "+elderDB.getBlood_group());
                jobtxt.setText("อาชีพ "+elderDB.getJob());
                drugtxt.setText("ยาที่แพ้ "+elderDB.getDrug_allergy());
                datetxt.setText("วันที่จอง "+request.getStart_date());
                timetxt.setText("เวลา "+request.getStart_time()+" นาฬิกา");

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
