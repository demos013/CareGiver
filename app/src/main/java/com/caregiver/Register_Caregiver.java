package com.caregiver;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.caregiver.Model.Caregiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class Register_Caregiver extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__caregiver);
        setOnClickAllEditText();
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


                } else {
                    startActivity(new Intent(Register_Caregiver.this,Authentication.class));
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

    public void onClickRegisterCaregiver(View v){
        Intent intent = new Intent(Register_Caregiver.this,Update_Profile_Caregiver.class);
        Caregiver caregiverDB = new Caregiver();
        caregiverDB.setUid(getIntent().getStringExtra("caregiver_id"));
        caregiverDB.setMobile_number( getIntent().getStringExtra("caregiver_mobile_number"));
        EditText edttemp = findViewById(R.id.register_caregiver_name);
        caregiverDB.setName(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_lastname);
        caregiverDB.setLastname(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_citizen_id);
        caregiverDB.setCitizen_id(edttemp.getText().toString());
        Spinner mSexSpiner = (Spinner) findViewById(R.id.register_caregiver_sex);
        caregiverDB.setSex(mSexSpiner.getSelectedItem().toString());
        edttemp = findViewById(R.id.register_caregiver_date_of_birth);
        caregiverDB.setDate_of_birth(edttemp.getText().toString());
        Spinner mJobSpiner = (Spinner) findViewById(R.id.register_caregiver_job);
        caregiverDB.setJob(mJobSpiner.getSelectedItem().toString());
        edttemp = findViewById(R.id.register_caregiver_experience);
        caregiverDB.setExperience(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_cost);
        caregiverDB.setCost(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_specialization);
        caregiverDB.setSpecialization(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_city);
        caregiverDB.setCity(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_district);
        caregiverDB.setDistrict(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_state);
        caregiverDB.setState(edttemp.getText().toString());

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mUsersRef = mRootRef.child("Caregiver");
        mUsersRef.child(caregiverDB.getUid()).setValue(caregiverDB);
        intent.putExtra("caregiverDB", (Serializable) caregiverDB);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void setOnClickAllEditText(){
        final EditText edttemp = findViewById(R.id.register_caregiver_date_of_birth);
        edttemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        edttemp.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                edttemp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(Register_Caregiver.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }


                });
            }
        });


        Spinner mSexSpiner = (Spinner) findViewById(R.id.register_caregiver_sex);
        String[] sex = getResources().getStringArray(R.array.sex);
        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sex);
        mSexSpiner.setAdapter(adapterSex);

        Spinner mJobSpiner = (Spinner) findViewById(R.id.register_caregiver_job);
        String[] job = getResources().getStringArray(R.array.caregiver_type);
        ArrayAdapter<String> adapterJob = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, job);
        mJobSpiner.setAdapter(adapterJob);

        if(SmartLocation.with(this).location().state().locationServicesEnabled()) {
            SmartLocation.with(this)
                    .location()
                    .config(LocationParams.LAZY)
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                            try {
                                //Log.d("fuck", "onLocationUpdated: ");
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();//ตำบล
                                String district = addresses.get(0).getSubAdminArea();//อำเถอ
                                String state = addresses.get(0).getAdminArea();//จังหวัด
                                String[] citysplit = city.split(" ");
                                String[] districtsplit = district.split(" ");

                                EditText edttemp = findViewById(R.id.register_caregiver_city);
                                edttemp.setText(citysplit[1]);
                                edttemp = findViewById(R.id.register_caregiver_district);
                                edttemp.setText(districtsplit[1]);
                                edttemp = findViewById(R.id.register_caregiver_state);
                                edttemp.setText(state);


                                Log.d(city +" "+district+" "+state+" fuck", "onLocationUpdated: ");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }



    }
}
