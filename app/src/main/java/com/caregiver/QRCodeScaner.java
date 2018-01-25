package com.caregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.caregiver.Model.Care_Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class QRCodeScaner extends AppCompatActivity {

    // QREader
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private Care_Activity activity;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scaner);

        // Setup SurfaceView
        // -----------------
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);
        activity = (Care_Activity) getIntent().getSerializableExtra("activity");

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Init and Start with SurfaceView
        // -------------------------------

        // Init QREader
        // ------------
        qrEader = new QREader.Builder(QRCodeScaner.this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d(data +"="+activity.getStart_key(), "onDetected: ");

                if(activity.getStart_key().equalsIgnoreCase(data)){
                    Log.d("wowowowowow", "onDetected: ");
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mActivityRef = mRootRef.child("Care_Activity").child(activity.getElder_uid()+activity.getCaregiver_uid());
                    Map<String,Object> activityDetail = new HashMap<>();
                    activityDetail.put("start_check",true);
                    mActivityRef.updateChildren(activityDetail);
                    activity.setStart_check(true);
                    Intent intent = new Intent(QRCodeScaner.this,Booking_Elderly_Detail.class);
                    intent.putExtra("activity",activity);
                    startActivity(intent);
                }


            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();


        qrEader.initAndStart(mySurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cleanup in onPause()
        // --------------------
        qrEader.releaseAndCleanup();
    }



}