package com.example.slpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PassengerTicketBook extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private Map<Integer, String> bookedSeats = new HashMap<>();

    // Temporary hardcoded input parameters
    private int seatNumber = -1;
    private String dateString = "17-10-23";
    private String userId = "+94761231234";
    private String busNumber = "BA-4568";
    private String tripNumber = "2";
    private int seatCount = 54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);

        // Initialize firebase realtime database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Intent intent = getIntent();
        //String busNumber = intent.getStringExtra("vehicalenumber");
        //String route = intent.getStringExtra("route");
        loadDataAndDrawSeats();

        // Set book button action
        Button btn = findViewById(R.id.bookBtn);
        btn.setOnClickListener(view -> {
            if (seatNumber != -1) {
                btn.setEnabled(false);
                // If seat is already booked. Then reservation is removed.
                // This can be called only by clicking available seats or seats booked by user
                if (bookedSeats.containsKey(seatNumber)) {
                    // Remove Booking
                    databaseReference.child("Bus-Reservation")
                            .child(busNumber)
                            .child(tripNumber)
                            .child(dateString)
                            .child(String.valueOf(seatNumber))
                            .removeValue().addOnCompleteListener(value -> {
                                Toast.makeText(this, "Booking Removed Successfully.", Toast.LENGTH_SHORT).show();
                                loadDataAndDrawSeats();
                            });
                } else {
                    // Place booking
                    Map<String, Object> map = new HashMap<>();
                    map.put(String.valueOf(seatNumber), userId);
                    databaseReference.child("Bus-Reservation")
                            .child(busNumber)
                            .child(tripNumber)
                            .child(dateString).updateChildren(map).addOnCompleteListener(value -> {
                                Toast.makeText(this, "Booking Placed Successfully.", Toast.LENGTH_SHORT).show();
                                loadDataAndDrawSeats();
                            });
                }
            } else {
                Toast.makeText(this, "Please select a seat to place a booking.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method can be used to reload and re-render all data
    private void loadDataAndDrawSeats() {
        LinearLayout tempLinear = findViewById(R.id.scrollViewLinearLayout);
        tempLinear.removeAllViews();
        bookedSeats = new HashMap<>();
        databaseReference.child("Bus-Reservation")
                .child(busNumber)
                .child(tripNumber)
                .child(dateString).get().addOnCompleteListener(result -> {
                    DataSnapshot snapshot = result.getResult();
                    // Collect booked seats
                    for (DataSnapshot childSnap: snapshot.getChildren()) {
                        bookedSeats.put(Integer.valueOf(childSnap.getKey()), childSnap.getValue(String.class));
                    }
                    createBusSeats();
                });
    }

    // Dynamically create bus seats while adding spaces as necessary.
    private void createBusSeats() {
        Button bookBtn = findViewById(R.id.bookBtn);

        List<MaterialButton> buttonList = new ArrayList<>();
        LinearLayout linearLayout = findViewById(R.id.scrollViewLinearLayout);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        buttonLayoutParams.setMargins(10, 0, 10, 10);
        buttonLayoutParams.height = 200;

        LinearLayout.LayoutParams spaceLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        int addedSeatCount = 1;
        while (addedSeatCount != seatCount) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            int seatPerRow = 5;
            boolean flag;
            // Modify row before last row
            if (seatCount < 44) {
                flag = (seatCount < addedSeatCount + (seatPerRow * 3) && seatCount >= (addedSeatCount + 6));
            } else {
                flag = (seatCount < addedSeatCount + (seatPerRow * 2) && seatCount >= (addedSeatCount + 6));
            }
            if (flag) {
                seatPerRow = 3;
            } else if (seatCount <= addedSeatCount + 6) {
                // 6 seats for last row
                seatPerRow = 6;
            }

            // Create rows with calculated parameters
            for (int x = 0; x < seatPerRow; x++) {
                // Add space for row
                if (x == 2 && !flag && seatPerRow != 6) {
                    Space space = new Space(this);
                    space.setLayoutParams(spaceLayout);
                    rowLayout.addView(space);
                } else if (x == 0 && flag) {
                    for (int y = 0; y < 2; y++) {
                        Space space = new Space(this);
                        space.setLayoutParams(buttonLayoutParams);
                        rowLayout.addView(space);
                    }
                    Space space = new Space(this);
                    space.setLayoutParams(spaceLayout);
                    rowLayout.addView(space);
                }

                // generate buttons
                MaterialButton button = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
                button.setLayoutParams(buttonLayoutParams);
                button.setPadding(0, 0, 0, 0);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
                button.setText(String.format(Locale.getDefault(), "%02d", addedSeatCount));

                int seatNumberTemp = addedSeatCount;
                // Seat is booked, but by user
                if (bookedSeats.containsKey(seatNumberTemp) && bookedSeats.get(seatNumberTemp).equals(userId)) {
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(238, 153, 252)));
                    button.setOnClickListener(view -> {
                        bookBtn.setText("Remove BOOKING");
                        bookBtn.setEnabled(true);
                        seatNumber = seatNumberTemp;
                        for (MaterialButton btn: buttonList) {
                            if (!bookedSeats.containsKey(Integer.valueOf(String.valueOf(btn.getText())))) {
                                btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                            }
                        }
                        TextView bookNumber = findViewById(R.id.seatNumberBookView);
                        bookNumber.setText("Seat Number : " + seatNumberTemp);
                    });

                    // Seat is booked, not by user
                } else if (bookedSeats.containsKey(seatNumberTemp)) {
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    button.setOnClickListener(view -> {
                        Toast.makeText(this, "This seat is already booked.", Toast.LENGTH_SHORT).show();
                    });

                    // Seat is available
                } else {
                    button.setOnClickListener(view -> {
                        bookBtn.setText("BOOK");
                        bookBtn.setEnabled(true);
                        seatNumber = seatNumberTemp;
                        for (MaterialButton btn: buttonList) {
                            if (!bookedSeats.containsKey(Integer.valueOf(String.valueOf(btn.getText())))) {
                                btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                            }
                        }
                        TextView bookNumber = findViewById(R.id.seatNumberBookView);
                        bookNumber.setText("Seat Number : " + seatNumberTemp);
                        button.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(238, 153, 252)));
                    });
                }

                // Add created button to row
                rowLayout.addView(button);
                // Add buttons to a list to iterate later. Which is used to clear styles
                buttonList.add(button);

                // Stop iteration if all seats are generated
                if (addedSeatCount == seatCount) {
                    break;
                }
                addedSeatCount++;
            }

            // Add row to main view
            linearLayout.addView(rowLayout);
        }
    }

}
