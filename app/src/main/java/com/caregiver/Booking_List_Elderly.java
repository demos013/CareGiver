package com.caregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.caregiver.CustomListview.elderly_adapter;
import com.caregiver.CustomListview.elderly_booking_adapter;
import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Request_Care_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Booking_List_Elderly extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private ArrayList<Care_Activity> AllActivityDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__list__elderly);
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
                    getActivity();

                } else {
                    startActivity(new Intent(Booking_List_Elderly.this,Authentication.class));
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

    public void getActivity(){
        AllActivityDB = new ArrayList<>();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Care_Activity");
        Query query = mUsersRef.orderByChild("caregiver_uid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AllActivityDB.add(postSnapshot.getValue(Care_Activity.class));
                }
                updateElderlyListView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateElderlyListView(){
        Log.d("3", "updateElder: ");
        elderly_booking_adapter adapter = new elderly_booking_adapter(this,AllActivityDB);
        ListView listView = findViewById(R.id.booking_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Booking_List_Elderly.this,Booking_Elderly_Detail.class);
                intent.putExtra("activity",AllActivityDB.get(i));
                startActivity(intent);
            }
        });
    }

    public void onCickInBoxIntent(View view){
        Intent intent = new Intent(Booking_List_Elderly.this,CareGiver_in_box.class);
        startActivity(intent);
    }
}
