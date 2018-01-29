package com.caregiver;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.caregiver.CustomListview.elderly_adapter;
import com.caregiver.CustomListview.showcaregiverlistview;
import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Elder;
import com.caregiver.Model.Request_Care_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class CareGiver_in_box extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private ArrayList<Elder> AllElderlyDB;
    private ArrayList<Request_Care_Activity> AllRequestActivityDB;
    private Caregiver caregiverDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_giver_in_box);
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
                    getUserDB(user.getUid());
                    //setupNavigationBar();
                    updateLocation();
                    getRequestActivity();


                } else {
                    startActivity(new Intent(CareGiver_in_box.this,Authentication.class));
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

    public void signout(View view){
        mAuth.signOut();
    }

    public void getUserDB(String uid){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Caregiver").child(uid);
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                caregiverDB =  dataSnapshot.getValue(Caregiver.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void updateLocation(){
        if(SmartLocation.with(this).location().state().locationServicesEnabled()) {
            SmartLocation.with(this)
                    .location()
                    .config(LocationParams.LAZY)
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference mUsersRef = mRootRef.child("Caregiver").child(user.getUid());
                            Map<String,Object> newLocation = new HashMap<>();
                            newLocation.put("location",new com.caregiver.Model.Location(location.getLatitude(),location.getLongitude()));
                            mUsersRef.updateChildren(newLocation);
                        }
                    });
        } else {
            //do someting
        }
    }

    public void getRequestActivity(){
        AllRequestActivityDB = new ArrayList<>();
        AllElderlyDB = new ArrayList<>();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Request_Care_Activity");
        Query query = mUsersRef.orderByChild("caregiver_id").equalTo(user.getUid());
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                   AllRequestActivityDB.add(postSnapshot.getValue(Request_Care_Activity.class));
               }
               updateElderlyListView();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }

    public void getElderlyDB(String elder_uid){

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Elder");
        Query query = mUsersRef.orderByKey().equalTo(elder_uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        AllElderlyDB.add(postSnapshot.getValue(Elder.class));
                        Log.d("2", "updateElder: ");
                        Log.d(postSnapshot.getValue(Elder.class).getUid(), "updateElder: ");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



    }

    public void updateElderlyListView(){
        Log.d("3", "updateElder: ");
        elderly_adapter adapter = new elderly_adapter(this,AllRequestActivityDB);
        ListView listView = findViewById(R.id.care_giver_in_box_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CareGiver_in_box.this,Elderly_Detail.class);
                intent.putExtra("request",AllRequestActivityDB.get(i));
                startActivity(intent);
            }
        });
    }

    public void onCickBookingIntent(View view){
        Intent intent = new Intent(CareGiver_in_box.this,Booking_List_Elderly.class);
        startActivity(intent);
    }


}
