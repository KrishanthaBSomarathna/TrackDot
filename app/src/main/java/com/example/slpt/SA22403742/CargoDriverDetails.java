package com.example.slpt.SA22403742;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.example.slpt.SA22403810.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CargoDriverDetails extends AppCompatActivity {
    DatabaseReference databaseReference;

    private String mobile;
    EditText type,phone,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_driver_details);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        type = findViewById(R.id.vehicaltype);
        phone = findViewById(R.id.Phone);
        number = findViewById(R.id.vehicleNumber);

        mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();





        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namestr = (String) snapshot.child("Cargo Drivers").child(mobile).child("Phone Number").getValue();
                String num = (String) snapshot.child("Cargo Drivers").child(mobile).child("Vehical Number").getValue();
                String tpe = (String) snapshot.child("Cargo Drivers").child(mobile).child("Vehical Type").getValue();
                phone.setText(namestr);
                type.setText(tpe);
                number.setText(num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}