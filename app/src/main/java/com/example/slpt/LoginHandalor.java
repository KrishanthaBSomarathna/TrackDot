package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginHandalor extends AppCompatActivity {

    Button manthi,hasheef,ashfak,krishantha,reg,bus;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginhandalor);
        manthi = findViewById(R.id.manthi);
        hasheef = findViewById(R.id.hasheef);
        ashfak = findViewById(R.id.ashfak);
        krishantha = findViewById(R.id.krishantha);
        reg = findViewById(R.id.reg);
        bus = findViewById(R.id.busd);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){
            firebaseUser.getEmail();
        }


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
                startActivity(new Intent(LoginHandalor.this, PassengerTaxiBook.class));
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