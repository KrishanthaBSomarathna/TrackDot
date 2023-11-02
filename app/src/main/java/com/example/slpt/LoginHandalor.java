package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.SA22403292.PassengerTicketBook;
import com.example.slpt.SA22403810.BusDriverDetails;
import com.example.slpt.SA22403810.BusDriverView;
import com.example.slpt.SA22403810.PassengerMainView;
import com.example.slpt.SA22404350.Register;
import com.example.slpt.SA22410122.Entry2;
import com.example.slpt.SA22410122.SetLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginHandalor extends AppCompatActivity {

    Button manthi,hasheef,ashfak,krishantha,reg,bus,busview,drivertax;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_handalor);
        manthi = findViewById(R.id.manthi);
        hasheef = findViewById(R.id.hasheef);
        ashfak = findViewById(R.id.ashfak);
        krishantha = findViewById(R.id.krishantha);
        reg = findViewById(R.id.reg);
        bus = findViewById(R.id.busd);
        busview = findViewById(R.id.busview);
        drivertax=findViewById(R.id.button3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){
            firebaseUser.getEmail();
        }

        busview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, BusDriverView.class));
            }
        });
        drivertax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginHandalor.this, TaxiDriver.class));
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, Register.class));
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, BusDriverDetails.class));
            }
        });

        manthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, PassengerTicketBook.class));
            }
        });

        hasheef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, CargoDriver.class));
            }
        });
        ashfak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, Entry2.class));
            }
        });
        krishantha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, PassengerMainView.class));
            }
        });
    }





}