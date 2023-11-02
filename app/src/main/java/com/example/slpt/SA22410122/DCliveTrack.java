package com.example.slpt.SA22410122; // Update the package name to match your project

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.example.slpt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DCliveTrack extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference driverLocationRef;
    private DatabaseReference passengerLocationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dclive_track); // Update with your layout resource

        Button cancelTripButton = findViewById(R.id.cancelTripButton);
        cancelTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel trip button click
                Intent intent = new Intent(DCliveTrack.this, DriverCancellationActivity.class); // Update with your cancellation activity
                startActivity(intent);
            }
        });

        // Initialize Firebase Realtime Database references.
        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("drivers").child("driver_id_1").child("location"); // Update with your Firebase references
        passengerLocationRef = FirebaseDatabase.getInstance().getReference().child("passengers").child("passenger_id_1").child("location"); // Update with your Firebase references

        // Initialize the MapView and set up Google Maps.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set up map configuration settings.
        // For example, you can set the initial camera position and other map settings here.

        // Move the camera to a default location (e.g., a city center).
        LatLng defaultLocation = new LatLng(6.927079, 79.861244); // Replace with your default location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 12);
        mMap.moveCamera(cameraUpdate);

        // Start listening for driver and passenger location updates.
        listenForDriverLocationUpdates();
        listenForPassengerLocationUpdates();
    }

    private void listenForDriverLocationUpdates() {
        // Use ValueEventListener to listen for driver location updates from Firebase.
        driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve driver's location data from the dataSnapshot.
                Double driverLat = dataSnapshot.child("latitude").getValue(Double.class);
                Double driverLng = dataSnapshot.child("longitude").getValue(Double.class);

                if (driverLat != null && driverLng != null) {
                    // Create a marker for the driver's location and add it to the map.
                    LatLng driverLocation = new LatLng(driverLat, driverLng);
                    mMap.clear(); // Clear previous markers (optional)
                    mMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, if any, while retrieving driver location.
            }
        });
    }

    private void listenForPassengerLocationUpdates() {
        // Use ValueEventListener to listen for passenger location updates from Firebase.
        passengerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve passenger's location data from the dataSnapshot.
                Double passengerLat = dataSnapshot.child("latitude").getValue(Double.class);
                Double passengerLng = dataSnapshot.child("longitude").getValue(Double.class);

                if (passengerLat != null && passengerLng != null) {
                    // Create a marker for the passenger's location and add it to the map.
                    LatLng passengerLocation = new LatLng(passengerLat, passengerLng);
                    mMap.clear(); // Clear previous markers (optional)
                    mMap.addMarker(new MarkerOptions().position(passengerLocation).title("Passenger"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, if any, while retrieving passenger location.
            }
        });
    }
}
