package com.caregiver;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.caregiver.Model.Elder;
import com.caregiver.Model.Location;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Register_Elder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__elder);


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
        edttemp = findViewById(R.id.register_elder_blood_group);
        elderDB.setBlood_group(edttemp.getText().toString());
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


}
