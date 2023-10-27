package com.example.slpt.SA22410122;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;

public class CarSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection);

        ImageView carImageView = findViewById(R.id.carImageView);
        ImageView vanImageView = findViewById(R.id.vanImageView);
        ImageView bikeImageView = findViewById(R.id.bikeImageView);
        ImageView threeWheelImageView = findViewById(R.id.threeWheelImageView);



        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the driver details activity for a car
                startDriverDetailsActivity("Car");
            }

        });

        vanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the driver details activity for a van
                startDriverDetailsActivity("Van");
            }
        });

        bikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the driver details activity for a bike
                startDriverDetailsActivity("Bike");
            }
        });

        threeWheelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the driver details activity for a three-wheel vehicle
                startDriverDetailsActivity("Three Wheel");
            }
        });
    }

    private void startDriverDetailsActivity(String vehicleType) {
        // Create an Intent to start the driver details activity
        Intent intent = new Intent(this, DriverClientLiveTrackingActivity.class);
        intent.putExtra("vehicle_type", vehicleType);
        startActivity(intent);
    }
}
