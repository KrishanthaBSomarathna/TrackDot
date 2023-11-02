package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.SA22403292.TicketBookInitializer;
import com.example.slpt.SA22403742.CargoDriver;
import com.example.slpt.SA22403742.CargoDriverDetails;
import com.example.slpt.SA22403742.CargoUser;
import com.example.slpt.SA22403742.OrderDetails;
import com.example.slpt.SA22403742.OrderLocation;
import com.example.slpt.SA22403810.BusDriverDetails;
import com.example.slpt.SA22403810.BusDriverView;
import com.example.slpt.SA22403810.PassengerMainView;
import com.example.slpt.SA22403810.SplashScreen;
import com.example.slpt.SA22404350.Register;
import com.example.slpt.SA22410122.Entry2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginHandalor extends AppCompatActivity {

    Button manthi,hasheef,ashfak,krishantha,reg,bus,busview,main;
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
        main = findViewById(R.id.main);
        reg = findViewById(R.id.reg);
        bus = findViewById(R.id.busd);
        busview = findViewById(R.id.busview);

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
                startActivity(new Intent(LoginHandalor.this, TicketBookInitializer.class));
            }
        });

        hasheef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, CargoDriverDetails.class));
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
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginHandalor.this, SplashScreen.class));
            }
        });
    }





}
