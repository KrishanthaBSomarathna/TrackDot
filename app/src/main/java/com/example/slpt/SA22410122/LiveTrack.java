package com.example.slpt.SA22410122;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LiveTrack extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Button reachPassengerButton;
    private Button passengerAlertButton;
    private DatabaseReference passengerStatusRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reachPassengerButton = findViewById(R.id.reachPassengerButton);
        passengerAlertButton = findViewById(R.id.passengerAlertButton);

        // Initialize Firebase Realtime Database reference for passenger status
        passengerStatusRef = FirebaseDatabase.getInstance().getReference("PassengerStatus");

        reachPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the status in Firebase to inform the passenger that the driver has reached
                passengerStatusRef.setValue("Driver has reached");
                Toast.makeText(getApplicationContext(), "Driver has reached the passenger.", Toast.LENGTH_SHORT).show();
            }
        });

        passengerAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle passenger alert functionality
                // You can implement this part as needed (e.g., send a notification to the passenger).
                Toast.makeText(getApplicationContext(), "Passenger alerted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Example: Show a marker for the driver's location (change the coordinates to the actual driver's location)
        LatLng driverLocation = new LatLng(37.7749, -122.4194);
        Marker driverMarker = googleMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver"));
        driverMarker.showInfoWindow();

        // Example: Move the camera to the driver's location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 15));
    }
}
