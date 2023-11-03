package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.slpt.R;

public class Driver_dash extends AppCompatActivity {
    CardView adddriver,userdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash);


        adddriver = findViewById(R.id.driver);
        userdetails = findViewById(R.id.user);


        adddriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CargoDriverDetails.class));

            }
        });


    }
}