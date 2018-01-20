package com.caregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Signup_Select extends AppCompatActivity {

    private Boolean isElder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__select);

    }

    public void onClickElder(View v){
        isElder = true;
        Intent intent = new Intent(Signup_Select.this,Signup_by_phone.class);
        intent.putExtra("isElder",isElder);
        startActivity(intent);
    }

    public void onClickCareGiver(View v){
        isElder = false;
        Intent intent = new Intent(Signup_Select.this,Signup_by_phone.class);
        intent.putExtra("isElder",isElder);
        startActivity(intent);

    }

}
