package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class OrderDetails extends AppCompatActivity {

    EditText packageName, Weight, type, orderNumber, address, email, phone;
    DatabaseReference databaseReference;
    Button button;

    // Add variables for location
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        packageName = findViewById(R.id.pacageName);
        Weight = findViewById(R.id.Weight);
        type = findViewById(R.id.type);
        orderNumber = findViewById(R.id.ordernumber);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        // Request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            // Permission is already granted, so you can initialize the location services.
            initializeLocationServices();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        button = findViewById(R.id.save);
        String mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get and save the current location
                if (lastKnownLocation != null) {
                    double latitude = lastKnownLocation.getLatitude();
                    double longitude = lastKnownLocation.getLongitude();
                    // Save latitude and longitude to the database or use them as needed.
                    databaseReference.child("Order").child(mobile).child("latitude").setValue(latitude);
                    databaseReference.child("Order").child(mobile).child("longitude").setValue(longitude);
                }

                // Your existing code to save other order details
                databaseReference.child("Order").child(mobile).child("PackegeName").setValue(packageName.getText().toString());
                databaseReference.child("Order").child(mobile).child("Weight").setValue(Weight.getText().toString());
                databaseReference.child("Order").child(mobile).child("Type").setValue(type.getText().toString());
                databaseReference.child("Order").child(mobile).child("Order Number").setValue(orderNumber.getText().toString());
                databaseReference.child("Order").child(mobile).child("Phone NUmber").setValue(phone.getText().toString());
                databaseReference.child("Order").child(mobile).child("Address").setValue(address.getText().toString());
                databaseReference.child("Order").child(mobile).child("Email").setValue(email.getText().toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize location services
                initializeLocationServices();
            }
        }
    }

    private void initializeLocationServices() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

        locationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Location is available, you can use it.
                    lastKnownLocation = location;
                }
            }
        });
    }
}
