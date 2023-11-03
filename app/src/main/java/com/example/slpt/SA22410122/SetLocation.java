package com.example.slpt.SA22410122;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.slpt.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SetLocation extends AppCompatActivity {
    private EditText originEditText; // Input for current location
    private EditText destinationEditText;
    private TextView distanceTextView;
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        distanceTextView = findViewById(R.id.distanceTextView);
        originEditText = findViewById(R.id.originEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        Button calculateDistanceButton = findViewById(R.id.calculateDistanceButton);
        Button bookTaxiButton = findViewById(R.id.bookTaxiButton);

        calculateDistanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndCalculateDistance();
            }
        });

        bookTaxiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTaxi();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void checkAndCalculateDistance() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkLocationSettings();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getUserLocation();
                } catch (ApiException exception) {
                    Toast.makeText(SetLocation.this, "Location settings are not satisfied.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override

                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                userLocation = task.getResult();
                                calculateDistance();
                            } else {
                                Toast.makeText(SetLocation.this, "Location is not available.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission is not granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateDistance() {
        if (userLocation != null) {
            String origin = originEditText.getText().toString();
            String destination = destinationEditText.getText().toString();

            if (!origin.isEmpty() && !destination.isEmpty()) {
                double distance = calculateDistance(userLocation.getLatitude(), userLocation.getLongitude(), destination);
                String distanceText = String.format("%.2f km", distance);
                distanceTextView.setText("Distance: " + distanceText);
            } else {
                Toast.makeText(this, "Please enter both origin and destination.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private double calculateDistance(double startLat, double startLon, String destination) {
        // Implement your distance calculation logic here
        // You can use the Haversine formula or Google Directions API for accurate results
        // For simplicity, we'll return a random distance for demonstration purposes.
        return Math.random() * 100; // Random distance between 0 and 100 km
    }

    private void bookTaxi() {
        Intent intent = new Intent(SetLocation.this, CarSelection.class);
        startActivity(intent);
    }
}
