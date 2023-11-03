package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.slpt.R;

public class DashBoard extends AppCompatActivity {

    CardView order,user,driverinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        order = findViewById(R.id.orderlocation);
        user = findViewById(R.id.userdetails);
        driverinfo = findViewById(R.id.driverinfo);



        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrderLocation.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CargoUser.class));
            }
        });

        driverinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CargoDriverDetails.class));
            }
        });
    }
}