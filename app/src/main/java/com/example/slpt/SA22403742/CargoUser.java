package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CargoUser extends AppCompatActivity {

    EditText address,email,phone;

    DatabaseReference databaseReference;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_user);

        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        button = findViewById(R.id.save);
        String mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Cargo User").child(mobile).child("Phone NUmber").setValue(phone.getText().toString());
                databaseReference.child("Cargo User").child(mobile).child("Address").setValue(address.getText().toString());
                databaseReference.child("Cargo User").child(mobile).child("Email").setValue(email.getText().toString());
            }
        });









    }
}