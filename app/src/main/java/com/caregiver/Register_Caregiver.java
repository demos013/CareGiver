package com.caregiver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.caregiver.Model.Caregiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Register_Caregiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__caregiver);
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
        edttemp = findViewById(R.id.register_caregiver_date_of_birth);
        caregiverDB.setDate_of_birth(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_address);
        caregiverDB.setAddress(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_telephone);
        caregiverDB.setTelephone(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_specialization);
        caregiverDB.setSpecialization(edttemp.getText().toString());
        edttemp = findViewById(R.id.register_caregiver_carecenter_id);
        caregiverDB.setCarecenter_id(edttemp.getText().toString());

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
}
