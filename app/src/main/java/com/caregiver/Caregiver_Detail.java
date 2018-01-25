package com.caregiver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.caregiver.Model.Care_Activity;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
                file.delete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    public void onBookingCaregiver (View view){

        showDialogConfirmActivity(view);


    }

    public void showDialogConfirmActivity(final View view){
        final Dialog dialog = new Dialog(Caregiver_Detail.this);
        dialog.setContentView(R.layout.dialog_request_activity);


        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_request_activity_cancel);
        Button buttonAdd = (Button) dialog.findViewById(R.id.dialog_request_activity_ok);
        final EditText dateedt = dialog.findViewById(R.id.dialog_request_activity_date);
        dateedt.setInputType(InputType.TYPE_NULL);
        dateedt.setTextIsSelectable(true);
        final EditText timeedt = dialog.findViewById(R.id.dialog_request_activity_time);
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

                if (!dateedt.getText().toString().equals("")&&!timeedt.getText().toString().equals("")){
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mUsersRef = mRootRef.child("Request_Care_Activity");
                    Request_Care_Activity request = new Request_Care_Activity();
                    int confirmkey = generateRandomNumber();
                    request.setCaregiver_id(caregiverDB.getUid());
                    request.setElder_uid(user.getUid());
                    request.setConfirm_key(confirmkey);
                    request.setStart_date(dateedt.getText().toString());
                    request.setStart_time(timeedt.getText().toString());
                    mUsersRef.child(user.getUid()+caregiverDB.getUid()).setValue(request);
                    Button bookingbt = findViewById(R.id.caregiver_detail_booking_button);
                    bookingbt.setVisibility(View.GONE);
                    TextView confirmkeytxt = findViewById(R.id.caregiver_detail_confirm_key);
                    confirmkeytxt.setText(String.valueOf(confirmkey));
                    confirmkeytxt.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    Log.d("noterroe", "onClick: ");
                }
                else{
                    Log.d("error", "onClick: ");
                    if(dateedt.getText().toString().equals("")){
                        dateedt.setError("กรุณาใส่วันที่ต้องการทำจองกิจกรรมการดูแล");
                    }
                    if(timeedt.getText().toString().equals("")){
                        timeedt.setError("กรุณาใส่เวลาต้องการทำจองกิจกรรมการดูแล");
                    }
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
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateedt.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dateedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Caregiver_Detail.this, date, myCalendar
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
                mTimePicker = new TimePickerDialog(Caregiver_Detail.this, new TimePickerDialog.OnTimeSetListener() {
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

    public int generateRandomNumber() {
        int randomNumber;
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < 4; i++) {
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
