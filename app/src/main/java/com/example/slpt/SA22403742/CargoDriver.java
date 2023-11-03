package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CargoDriver extends AppCompatActivity {

    EditText vehicleNumber,vehicaltype,phone;

    DatabaseReference databaseReference;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_driver);

        phone = findViewById(R.id.phone);
        vehicaltype = findViewById(R.id.vehicaltype);
        vehicleNumber = findViewById(R.id.vehicaltype);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        button = findViewById(R.id.save);
        String mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CargoDriver.this, "Saved", Toast.LENGTH_SHORT).show();
                databaseReference.child("Cargo Drivers").child(mobile).child("Phone Number").setValue(phone.getText().toString());
                databaseReference.child("Cargo Drivers").child(mobile).child("Vehical Type").setValue(vehicaltype.getText().toString());
                databaseReference.child("Cargo Drivers").child(mobile).child("Vehical Number").setValue(vehicleNumber.getText().toString());
            }
        });









    }
}