package com.caregiver;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.caregiver.CustomListview.navigationlistview;
import com.caregiver.CustomListview.showcaregiverlistview;
import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Elder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.nlopez.smartlocation.SmartLocation;

public class Listview_Show extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private Elder elderDB;
    private ArrayList<Caregiver> AllCaregiverDB;

    private String[] mDrawerTitle = {"รายการจอง", "Sign out"};
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview__show);
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
                    setupNavigationBar();
                    //updateLocation();
                    getCaregiverDB();



                } else {
                    startActivity(new Intent(Listview_Show.this,Authentication.class));
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


    public void setupNavigationBar(){
        //navigationbar
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mListView = (ListView) findViewById(R.id.drawer);
        navigationlistview adapter = new navigationlistview(this,mDrawerTitle);
        mListView.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                if(arg2==0){
                    Intent intent = new Intent(Listview_Show.this,Booking_List_Caregiver.class);
                    startActivity(intent);

                }
                if(arg2==1){
                    mAuth.signOut();
                }


            }
        });

        //toggle

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Care Giver</font>"));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,   // Context
                mDrawerLayout,  // DrawerLayout
                R.drawable.ic_drawer, // รูปภาพที่จะใช้
                R.string.drawer_open // ค่า String ในไฟล์ strings.xml

        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("open", "onDrawerOpened: ");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.location_map:
                Intent intent = new Intent(Listview_Show.this,Map_Show.class);
                startActivity(intent);
                return true;
            case R.id.filter:
                Log.d("filter", "onOptionsItemSelected: ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
         menu.getItem(1).setIcon(R.drawable.location_map);
        return true;
    }

    public void getCaregiverDB(){
        AllCaregiverDB = new ArrayList<>();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Caregiver");
        Query query = mUsersRef.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    AllCaregiverDB.add(postSnapshot.getValue(Caregiver.class));
                    Log.d(postSnapshot.getValue(Caregiver.class).getUid(), "onDataChange: ");
                }
                updateCaregiverListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateCaregiverListView(){
        showcaregiverlistview adapter = new showcaregiverlistview(this,AllCaregiverDB);
        Log.d("it me", "updateCaregiverListview: ");
        ListView listView = findViewById(R.id.listview_show_caregiver_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Listview_Show.this,Caregiver_Detail.class);
                intent.putExtra("caregiverDB",AllCaregiverDB.get(i));
                startActivity(intent);
            }
        });
    }
}
