package com.example.slpt.SA22410122;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetLocation extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText destinationEditText;
    private LocationManager locationManager;
    private TextView distanceTextView;
    private Location userLocation;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        distanceTextView = findViewById(R.id.distanceTextView);
        destinationEditText = findViewById(R.id.destinationEditText);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseRef = FirebaseDatabase.getInstance().getReference();

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void checkAndCalculateDistance() {
        if (isGPSEnabled()) {
            userLocation = getUserLocation();
            calculateDistance();
        } else {
            enableGPS();
        }
    }

    private boolean isGPSEnabled() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void enableGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Turn on GPS to use this feature.")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void calculateDistance() {
        String destination = destinationEditText.getText().toString();

        if (!destination.isEmpty() && userLocation != null) {
            String origin = userLocation.getLatitude() + "," + userLocation.getLongitude();
            String apiKey = "AIzaSyD_-WWgpw7jG6H9yfj3zR7mvJV9baKPVMQ";

            String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    "&key=" + apiKey;

            new CalculateDistanceTask().execute(url);
        } else {
            Toast.makeText(this, "Please enter a destination or ensure GPS is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    private class CalculateDistanceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String url = params[0];
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray routes = jsonObject.getJSONArray("routes");
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONObject distance = leg.getJSONObject("distance");

                return distance.getString("text");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String distance) {
            if (distance != null) {
                distanceTextView.setText("Distance: " + distance);
                saveDataToFirebase(userLocation, destinationEditText.getText().toString(), distance);
            } else {
                Toast.makeText(SetLocation.this, "Failed to calculate distance.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDataToFirebase(Location userLocation, String destination, String distance) {
        if (userLocation != null) {
            String userLocationText = "Lat: " + userLocation.getLatitude() + ", Lon: " + userLocation.getLongitude();
            LocationData locationData = new LocationData(userLocationText, destination, distance);
            String dataId = firebaseRef.push().getKey();
            firebaseRef.child(dataId).setValue(locationData);
        }
    }

    private void bookTaxi() {
        Intent intent = new Intent(SetLocation.this, CarSelection.class);
        startActivity(intent);
    }

    private class LocationData {
        public LocationData(String userLocationText, String destination, String distance) {

        }
    }
}
