package com.example.slpt.SA22410122;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverconfirmationActivity extends AppCompatActivity {
    private DatabaseReference driversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverconfirmation);

        // Initialize Firebase Realtime Database reference.
        driversRef = FirebaseDatabase.getInstance().getReference().child("https://fir-2-cdbd5-default-rtdb.firebaseio.com/Driver");

        // Retrieve and display driver details (for example, driver_id_1).
        retrieveDriverDetails("driver_id_1");

        // Set up the button to confirm pickup and transition to DriverClientLiveTracking activity.
        Button confirmPickupButton = findViewById(R.id.confirmPickupButton);
        confirmPickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle driver selection and transition to DriverClientLiveTracking activity.
                Intent intent = new Intent(DriverconfirmationActivity.this, DriverClientLiveTrackingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveDriverDetails(String driverId) {
        driversRef.child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String mobile = dataSnapshot.child("mobile").getValue(String.class);
                    String vehicleNumber = dataSnapshot.child("vehicle_number").getValue(String.class);

                    // Display driver details in your UI.
                    // (Code for displaying driver details in your UI goes here.)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors, if necessary.
            }
        });
    }
}
