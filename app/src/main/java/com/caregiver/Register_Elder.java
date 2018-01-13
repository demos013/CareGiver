package com.caregiver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.caregiver.Model.Elder;

public class Register_Elder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__elder);
    }

    public void onClickRegisterElder(View v){
        Intent intent = new Intent(Register_Elder.this,Update_Profile_Elder.class);
        EditText txttemp = findViewById(R.id.register_elder_name);
        String elder_name = txttemp.getText().toString();
        Elder fdfdf =new Elder();
        fdfdf.setAddress();

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
