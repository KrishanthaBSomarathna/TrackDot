package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
     EditText editTextPhoneNumber, editTextVerificationCode;
    private Button buttonSendOTP, buttonVerifyOTP;

    LinearLayout otp,sendotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        buttonSendOTP = findViewById(R.id.buttonSendOTP);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);

//
        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserType = ((Spinner) findViewById(R.id.spinnerUserType)).getSelectedItem().toString();

//

                if(editTextPhoneNumber.getText().toString() == " "){
                    Toast.makeText(getApplicationContext(),"Enter Phone Number",Toast.LENGTH_LONG).show();
                }
                else {

                    if (UserType=="UserTypes"){
                        Toast.makeText(getApplicationContext(),"Please Select User Type", Toast.LENGTH_LONG).show();
                    }

                }
//                sendVerificationCode();
            }
        });

        otp = findViewById(R.id.otplayout);
        sendotp = findViewById(R.id.phonelayout);

        otp.setVisibility(View.GONE);


        buttonVerifyOTP.setOnClickListener(v -> {

            String UserType = ((Spinner) findViewById(R.id.spinnerUserType)).getSelectedItem().toString();
            Toast.makeText(getApplicationContext(),"Please Select User Type", Toast.LENGTH_LONG);
            if (UserType!="User Types")
            {
                String code = editTextVerificationCode.getText().toString().trim();
                verifyVerificationCode(code);
            }
            else {

            }
        });
    }

    private void sendVerificationCode() {
        // Get the selected country code from the Spinner
        String selectedCountryCode = ((Spinner) findViewById(R.id.spinnerCountryCode)).getSelectedItem().toString();

        // Get the entered phone number
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        // Remove non-numeric characters from the country code
        String countryCode = selectedCountryCode.replaceAll("[^0-9]", "");

        // Concatenate the country code and phone number
        String fullPhoneNumber = "+" + countryCode + phoneNumber;

        // Initiate phone number verification
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullPhoneNumber,   // The combined phone number with country code
                60,                // Timeout duration
                TimeUnit.SECONDS,  // Timeout unit
                this,              // Activity
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "Verification Failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Register.this.verificationId = verificationId;
                        Toast.makeText(getApplicationContext(), "OTP Code Sent", Toast.LENGTH_SHORT).show();
                        otp.setVisibility(View.VISIBLE);
                        sendotp.setVisibility(View.GONE);
                    }
                });
    }


    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Phone authentication successful
                        startActivity(new Intent(Register.this, LoginHandalor.class));
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }
}
