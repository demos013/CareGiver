package com.caregiver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Signup_by_phone extends AppCompatActivity {

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private Boolean isElder;
    private boolean isSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_by_phone);
        mAuth = FirebaseAuth.getInstance();
        isElder = getIntent().getBooleanExtra("iselder",true);
        isSignIn = getIntent().getBooleanExtra("isSignIn", false);




    }

    public void onSubmit(View view){
        EditText phoneedt = findViewById(R.id.sign_up_by_phone_button_get_otp);
        sendCode(phoneedt.getText().toString());
    }

    public void sendCode(String phone){
        FirebaseApp.initializeApp(this);
        Log.d(phone, "sendCode: ");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.d("s", "onVerificationCompleted:" + phoneAuthCredential);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // ...
                            Log.d("Invalid request", "onVerificationFailed: ");
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Log.d("SMS quota ", "onVerificationFailed: ");
                            // The SMS quota for the project has been exceeded
                            // ...
                        }

                    }
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d("", "onCodeSent:" + verificationId);

                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;

                        // ...
                    }
                });


    }

    public void verify(View view){
        PinView pinView = (PinView) findViewById(R.id.sign_up_by_phone_pin_OTP);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, pinView.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            PinView pinView = (PinView) findViewById(R.id.sign_up_by_phone_pin_OTP);
                            pinView.setText(credential.getSmsCode());
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            if(isSignIn){
                                Intent intent = new Intent(Signup_by_phone.this , Map_Show.class);
                                startActivity(intent);
                            }
                            else{
                                if(isElder){
                                    Intent intent = new Intent(Signup_by_phone.this , Register_Elder.class);
                                    intent.putExtra("elder_id",user.getUid());
                                    intent.putExtra("elder_mobile_number",user.getPhoneNumber());
                                    startActivity(intent);

                                }
                                else{
                                    Intent intent = new Intent(Signup_by_phone.this , Register_Caregiver.class);
                                    intent.putExtra("caregiver_id",user.getUid());
                                    intent.putExtra("caregiver_id_mobile_number",user.getPhoneNumber());
                                    startActivity(intent);

                                }
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("failure", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}


