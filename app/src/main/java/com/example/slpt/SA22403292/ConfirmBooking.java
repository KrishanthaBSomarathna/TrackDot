package com.example.slpt.SA22403292;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.slpt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConfirmBooking extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private List<Integer> seatNumber = new ArrayList<>();
    private String dateString = "17-10-23";
    private String userId = "+94761231234";
    private String busNumber = "BA-4568";
    private String tripNumber = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        tripNumber = intent.getStringExtra("tripNumber");
        busNumber = intent.getStringExtra("route");

        ((TextView) findViewById(R.id.sourceName)).setText(intent.getStringExtra("start"));
        ((TextView) findViewById(R.id.destinationName)).setText(intent.getStringExtra("end"));
        ((TextView) findViewById(R.id.routeNumber)).setText(busNumber);
    }
}
