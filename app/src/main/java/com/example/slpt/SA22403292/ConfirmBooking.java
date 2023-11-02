package com.example.slpt.SA22403292;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slpt.R;
import com.example.slpt.SA22403810.PassengerMainView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {

    Dialog loadingView;

    private DatabaseReference databaseReference;

    private int[] seatNumbers = {};
    private String dateString = "17-10-23";
    private String userId = "999";
    private String busNumber = "BA-4568";
    private String tripNumber = "2";
    private float pricePerSeat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim();

        Intent intent = getIntent();
        tripNumber = intent.getStringExtra("trip");
        busNumber = intent.getStringExtra("route");
        pricePerSeat = intent.getFloatExtra("pricePerSeat", 0);
        seatNumbers = intent.getIntArrayExtra("seatNumbers");
        dateString = intent.getStringExtra("dateString");

        ((TextView) findViewById(R.id.sourceName)).setText(intent.getStringExtra("start"));
        ((TextView) findViewById(R.id.destinationName)).setText(intent.getStringExtra("end"));
        ((TextView) findViewById(R.id.routeNumber)).setText(busNumber);
        ((TextView) findViewById(R.id.dateView)).setText(dateString.replaceAll("-", "/"));
        ((TextView) findViewById(R.id.seat_count)).setText(String.valueOf(seatNumbers.length));
        ((TextView) findViewById(R.id.price_per_seat)).setText(String.format(Locale.getDefault(), "%.2f", pricePerSeat));

        Float totalPrice = (seatNumbers.length * pricePerSeat);
        ((TextView) findViewById(R.id.total_price)).setText(String.format(Locale.getDefault(), "%.2f", totalPrice));

        String seats = "";
        for (Integer seatNum : seatNumbers) {
            if (seats.isEmpty()) {
                seats += seatNum;
            } else {
                seats += (", " + seatNum);
            }
        }
        ((TextView) findViewById(R.id.selected_seats)).setText(seats);
        Button confirmButton = findViewById(R.id.confirm_booking);
        confirmButton.setOnClickListener(view -> {
            confirmButton.setEnabled(false);
            placeBooking();
        });
    }

    private void placeBooking() {
        Map<String, Object> map = new HashMap<>();
        for (int seatNumber : seatNumbers) {
            map.put(String.valueOf(seatNumber), userId);
        }
        loadingView = new Dialog(this);
        loadingView.setContentView(R.layout.loading_model_layout);
        loadingView.setCancelable(false);
        loadingView.show();
        databaseReference.child("Bus-Reservation")
                .child(busNumber)
                .child(tripNumber)
                .child(dateString).updateChildren(map).addOnCompleteListener(value -> {
                    loadingView.dismiss();
                    if (value.getException() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Error");
                        builder.setMessage("An error occurred while saving your data. Please try again.");
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Log.e("ERROR", "Error: ", value.getException());
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Success");
                        builder.setMessage("Your booking placed successfully!");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(this, PassengerMainView.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }

}
