package com.caregiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caregiver.CustomListview.navigationlistview;
import com.caregiver.CustomWindowAdapter.CustomWindowAdapter;
import com.caregiver.Model.Care_Activity;
import com.caregiver.Model.Caregiver;
import com.caregiver.Model.Elder;
import com.caregiver.Model.Review;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class Map_Show extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private Elder elderDB;
    private ArrayList<Caregiver> caregiverDB;
    private ArrayList<Float> sumScore;
    private ArrayList<Integer> sumReview;

    private HashMap<Marker,Caregiver> hashCaregiver;
    private HashMap<Marker,Float> hashScore;
    private HashMap<Marker,Integer> hashReview_num;


    private GoogleMap mMap;

    private String[] mDrawerTitle = {"รายการจอง", "Sign out"};
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    public void onStart() {
        super.onStart();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_map);
        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getUserDB(user.getUid());
                    setupNavigationBar();
                    updateLocation();
                    getActivityReview();
                    getCaregiverDB();


                } else {
                    startActivity(new Intent(Map_Show.this,Authentication.class));
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
        mMap.clear();
        SmartLocation.with(this)
                .location()
                .stop();
    }

    public void signout(View view){
        mAuth.signOut();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                if(!arg0.getTitle().equalsIgnoreCase("คุณอยู่นี่")){
                    Log.d(arg0.getId(), "onStart: ");
                    Intent intent = new Intent(Map_Show.this,Caregiver_Detail.class);
                    intent.putExtra("caregiverDB",hashCaregiver.get(arg0));
                    startActivity(intent);
                }

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                    Intent intent = new Intent(Map_Show.this,Booking_List_Caregiver.class);
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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.location_map:
                Intent intent = new Intent(Map_Show.this,Listview_Show.class);
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
        return true;
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
                            DatabaseReference mUsersRef = mRootRef.child("Elder").child(user.getUid());
                            Map<String,Object> newLocation = new HashMap<>();
                            newLocation.put("location",new com.caregiver.Model.Location(location.getLatitude(),location.getLongitude()));
                            mUsersRef.updateChildren(newLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13.0f));
                            Marker now;

                            LatLng IAmHere = new LatLng(location.getLatitude(), location.getLongitude());
                            now = mMap.addMarker(new MarkerOptions().position(IAmHere)
                                    .title("คุณอยู่นี่")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_elder)));





                        }
                    });
        } else {
            //do someting
        }
    }

    public void getUserDB(String uid){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Elder").child(uid);
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               elderDB =  dataSnapshot.getValue(Elder.class);
                Log.d(elderDB.getUid(), "onDataChange: ");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getCaregiverDB(){
        caregiverDB = new ArrayList<>();
        hashCaregiver = new HashMap<>();
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mCaregiverRef = mRootRef.child("Caregiver");
        Query query = mCaregiverRef.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    caregiverDB.add(postSnapshot.getValue(Caregiver.class));


                }
                updateCaregiverLocation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getActivityReview(){

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mBookingActivityRef = mRootRef.child("Care_Activity");
        Query query = mBookingActivityRef.orderByChild("elder_uid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue(Care_Activity.class).getFinish_check()){
                        Intent intent = new Intent(Map_Show.this,Rating.class);
                        intent.putExtra("activity",postSnapshot.getValue(Care_Activity.class));
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateCaregiverLocation(){


        for(Caregiver caregiver : caregiverDB){
            LatLng caregiverLocation = new LatLng(caregiver.getLocation().getLatitude(), caregiver.getLocation().getLongitude());
            Marker marker= mMap.addMarker(new MarkerOptions().position(caregiverLocation).title(caregiver.getName()+" "+caregiver.getLastname())
                    .snippet(caregiver.getJob()+"\nค่าบริการ "+caregiver.getCost()+" บาท")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle_caregiver)));
/*          Log.d(String.valueOf(sumReview.size()), "updateCaregiverLocation: ");
            Log.d(String.valueOf(sumScore.size()), "updateCaregiverLocation: ");*/

            hashCaregiver.put(marker,caregiver);

        }
        //mMap.setInfoWindowAdapter(new CustomWindowAdapter((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE),hashCaregiver));


    }




}
