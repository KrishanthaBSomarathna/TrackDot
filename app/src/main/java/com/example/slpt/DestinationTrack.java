package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class DestinationTrack extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private TextView distanceTextView;
    private Button endTripButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_track);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        distanceTextView = findViewById(R.id.distanceTextView);
        endTripButton = findViewById(R.id.endTripButton);

        // Replace the following with real-time location tracking and route calculation
        LatLng driverLocation = new LatLng(37.7749, -122.4194);
        LatLng passengerLocation = new LatLng(37.7749, -122.4190);

        // Example: Calculate the distance (in meters) between driver and passenger
        double distance = calculateDistance(driverLocation, passengerLocation);

        // Update the distanceTextView with the calculated distance
        distanceTextView.setText("Distance: " + distance + " meters");

        endTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle end trip action, e.g., save trip details and navigate to the AmountDetailActivity
                Intent intent = new Intent(DestinationTrack.this, AmountDetail.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Example: Show a static route between driver and passenger
        LatLng driverLocation = new LatLng(37.7749, -122.4194);
        LatLng passengerLocation = new LatLng(37.7749, -122.4190);

        googleMap.addPolyline(new PolylineOptions()
                .add(driverLocation, passengerLocation)
                .width(5)
                .color(0xFF0000FF)); // Blue color

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 15));
    }

    // Example function to calculate distance between two LatLng points
    private double calculateDistance(LatLng point1, LatLng point2) {
        // Replace with your own distance calculation logic (e.g., Haversine formula)
        // This is a simplified example
        return Math.sqrt(Math.pow(point1.latitude - point2.latitude, 2) +
                Math.pow(point1.longitude - point2.longitude, 2)) * 111.32 * 1000;
    }
}
