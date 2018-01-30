package com.caregiver;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.caregiver.Model.Elder;
import com.caregiver.Model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register_Elder extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__elder);
        setOnClickAllEditText();


    }

    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {


                } else {
                    startActivity(new Intent(Register_Elder.this,Authentication.class));
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

    public void onClickRegisterElder(View v){
        Intent intent = new Intent(Register_Elder.this,Update_Profile_Elder.class);
        Elder elderDB = new Elder();
        elderDB.setUid(getIntent().getStringExtra("elder_id"));
        elderDB.setMobile_number( getIntent().getStringExtra("elder_mobile_number"));
        EditText edttemp = findViewById(R.id.register_elder_name);
        elderDB.setName(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_lastname);
        elderDB.setLastname(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_citizen_id);
        elderDB.setCitizen_id(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_date_of_birth);
        elderDB.setDate_of_birth(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_job);
        elderDB.setJob(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_address);
        elderDB.setAddress(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_telephone);
        elderDB.setTelephone(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_drug_allergy);
        elderDB.setDrug_allergy(edttemp.getText().toString());
        Spinner mBloodSpiner = (Spinner) findViewById(R.id.register_elder_blood_group);
        elderDB.setBlood_group(mBloodSpiner.getSelectedItem().toString());
        Spinner mSexSpiner = (Spinner) findViewById(R.id.register_elder_sex);
        elderDB.setSex(mSexSpiner.getSelectedItem().toString());
        edttemp = findViewById(R.id.register_elder_relative_name);
        elderDB.setRelative_name(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_relative_lastname);
        elderDB.setRelative_lastname(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_elder_relative_telephone);
        elderDB.setRelative_mobile_number(edttemp.getText().toString());
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("Elder");
        mUsersRef.child(elderDB.getUid()).setValue(elderDB);
        intent.putExtra("elderDB", (Serializable) elderDB);
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
        final EditText edttemp = findViewById(R.id.register_elder_date_of_birth);
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
                        new DatePickerDialog(Register_Elder.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }


                });
            }
        });


        Spinner mSexSpiner = (Spinner) findViewById(R.id.register_elder_sex);
        String[] sex = getResources().getStringArray(R.array.sex);
        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sex);
        mSexSpiner.setAdapter(adapterSex);

        Spinner mBloodSpiner = (Spinner) findViewById(R.id.register_elder_blood_group);
        String[] blood = getResources().getStringArray(R.array.blood_group);
        ArrayAdapter<String> adapterBlood = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, blood);
        mBloodSpiner.setAdapter(adapterBlood);

    }


}
