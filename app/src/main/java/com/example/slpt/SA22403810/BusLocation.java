package com.example.slpt.SA22403810;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class BusLocation extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private MapView mapView;
    private DatabaseReference databaseReference;
    private Double busLat, busLon;
    private static String vehiclanumber;
    private final Handler handler = new Handler();
    private final int delayMillis = 5000; // 5 seconds
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check if location permission is granted (for Android 6.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Start listening for location updates
            startLocationUpdates();
        }

        Intent intent = getIntent();
        busLat = intent.getDoubleExtra("lat", 0);
        busLon = intent.getDoubleExtra("lon", 0);
        vehiclanumber = intent.getStringExtra("vehiclenumber");

        // Initial database read
        getLatLngByVehicleNum(vehiclanumber);
    }

    private final Runnable databaseReadRunnable = new Runnable() {
        @Override
        public void run() {
            getLatLngByVehicleNum(vehiclanumber);
            // Schedule the next execution
            handler.postDelayed(this, delayMillis);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Start the periodic task when the activity is resumed
        handler.postDelayed(databaseReadRunnable, delayMillis);
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(databaseReadRunnable);
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for the bus location
        LatLng busLocation = new LatLng(busLat, busLon);
        mMap.addMarker(new MarkerOptions().position(busLocation).title("Bus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 16));

        // Add user's location to the map
        showUserLocation();
    }

    private void startLocationUpdates() {
        // Request location updates every 5 seconds with a distance change of 10 meters
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update user's location on the map
        showUserLocation();
    }

    private void showUserLocation() {
        // Check if the map is ready and the location is available
        if (mMap != null && locationManager != null) {
            // Get the user's last known location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                // Add a marker at the user's location
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

                // Move camera to user's location
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
            }
        }
    }

    private void getLatLngByVehicleNum(String vehicleNum) {
        DatabaseReference busDriversRef = databaseReference.child("Bus Drivers");

        busDriversRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> driverData = (Map<String, Object>) driverSnapshot.getValue();

                    if (driverData != null && driverData.containsKey("vehicleNum")) {
                        String currentVehicleNum = (String) driverData.get("vehicleNum");

                        if (currentVehicleNum != null && currentVehicleNum.equals(vehicleNum)) {
                            // VehicleNum matches, retrieve latitude and longitude
                            Double latitude = (Double) driverData.get("Latitude");
                            Double longitude = (Double) driverData.get("Longitude");

                            // Update map and show toast on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateMap(latitude, longitude);
                                }
                            });

                            break; // Exit the loop since we found the match
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });
    }

    private void updateMap(Double latitude, Double longitude) {
        if (mMap != null) {
            // Clear existing markers
            mMap.clear();

            // Add a marker for the bus location
            LatLng busLocation = new LatLng(busLat, busLon);
            mMap.addMarker(new MarkerOptions().position(busLocation).title("Bus"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(busLocation));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 16));

            // Add a marker at the new location using latitude and longitude
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title("Your Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        finish();
        Animatoo.INSTANCE.animateSwipeRight(BusLocation.this);
    }
}
