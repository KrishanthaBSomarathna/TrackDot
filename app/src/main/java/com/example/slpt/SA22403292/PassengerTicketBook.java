package com.example.slpt.SA22403292;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slpt.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PassengerTicketBook extends AppCompatActivity {

    Dialog loadingView;

    private DatabaseReference databaseReference;

    private Map<Integer, String> bookedSeats = new HashMap<>();

    // Temporary hardcoded input parameters
    private List<Integer> seatNumbers = new ArrayList<>();
    private String dateString = "17-10-23";
    private String userId = "999";
    private String busNumber = "BA-4568";
    private boolean isStartToEnd = false;
    private String tripNumber = "2";
    private int seatCount = 54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);
        userId = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim();

        loadingView = new Dialog(this);
        loadingView.setContentView(R.layout.loading_model_layout);
        loadingView.setCancelable(false);
        loadingView.show();

        // Initialize firebase realtime database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        isStartToEnd = intent.getBooleanExtra("isStartToEnd", false);
        if (isStartToEnd) {
            tripNumber = "1";
        } else {
            tripNumber = "2";
        }
        busNumber = intent.getStringExtra("route");

        ((TextView) findViewById(R.id.sourceName)).setText(intent.getStringExtra("start"));
        ((TextView) findViewById(R.id.destinationName)).setText(intent.getStringExtra("end"));
        ((TextView) findViewById(R.id.routeNumber)).setText("(" + busNumber + ")");

        TextView dateView = findViewById(R.id.dateView);

        Button calendarBtn = findViewById(R.id.changeDateBtn);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateString = (
                String.format(Locale.getDefault(), "%02d", day) + "-" +
                        String.format(Locale.getDefault(), "%02d", (month + 1)) + "-" + year
        );
        dateView.setText(dateString.replaceAll("-", "/"));

        calendarBtn.setOnClickListener(view -> {
            // Reference : https://www.geeksforgeeks.org/datepicker-in-android/
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PassengerTicketBook.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        dateString = (
                                String.format(Locale.getDefault(), "%02d", dayOfMonth) + "-" +
                                        String.format(Locale.getDefault(), "%02d", (monthOfYear + 1)) + "-" + year1
                        );
                        dateView.setText(dateString.replaceAll("-", "/"));
                        loadingView.show();
                        loadDataAndDrawSeats();
                    },
                    year,
                    month,
                    day
            );
            // Set min date to today
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            Calendar maxDate = Calendar.getInstance();
            // Set max date to one month from today
            maxDate.add(Calendar.MONTH, 1);
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();
        });

        // Set book button action
        Button bookBtn = findViewById(R.id.bookBtn);
        bookBtn.setOnClickListener(view -> {
            if (!seatNumbers.isEmpty()) {
                Intent confirmPage = new Intent(this, ConfirmBooking.class);
                confirmPage.putExtra("start", intent.getStringExtra("start"));
                confirmPage.putExtra("end", intent.getStringExtra("end"));
                confirmPage.putExtra("trip", tripNumber);
                confirmPage.putExtra("route", busNumber);
                confirmPage.putExtra("dateString", dateString);
                confirmPage.putExtra("pricePerSeat", intent.getFloatExtra("pricePerSeat", 0));

                int[] seatNumTem = new int[seatNumbers.size()];
                for (int i = 0; i < seatNumbers.size(); i++) {
                    seatNumTem[i] = seatNumbers.get(i);
                }
                confirmPage.putExtra("seatNumbers", seatNumTem);
                startActivity(confirmPage);
            } else {
                Toast.makeText(this, "Please select a seat to place a booking.", Toast.LENGTH_SHORT).show();
            }
        });

        loadDataAndDrawSeats();
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
                    loadingView.dismiss();
                    if (result.getException() != null) {
                        Log.e("ERROR", "Error: ", result.getException());
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Error");
                        builder.setMessage("An error occurred while retrieving data. Please try again.");
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        DataSnapshot snapshot = result.getResult();
                        // Collect booked seats
                        for (DataSnapshot childSnap : snapshot.getChildren()) {
                            bookedSeats.put(Integer.valueOf(childSnap.getKey()), childSnap.getValue(String.class));
                        }
                        createBusSeats();
                    }
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
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(1, 50, 32)));
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(view -> {
                        Toast.makeText(this, "To remove your booking, visit my booking page.", Toast.LENGTH_SHORT).show();
                    });

                    // Seat is booked, not by user
                } else if (bookedSeats.containsKey(seatNumberTemp)) {
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(view -> {
                        Toast.makeText(this, "This seat is already booked.", Toast.LENGTH_SHORT).show();
                    });

                    // Seat is available
                } else {
                    button.setOnClickListener(view -> {
                        if (seatNumbers.contains(seatNumberTemp)) {
                            seatNumbers.remove(seatNumbers.indexOf(seatNumberTemp));
                        } else {
                            seatNumbers.add(seatNumberTemp);
                        }
                        bookBtn.setEnabled(!seatNumbers.isEmpty());
                        TextView bookNumber = findViewById(R.id.seatNumberBookView);
                        String seats = "";
                        for (Integer seatNum : seatNumbers) {
                            if (seats.isEmpty()) {
                                seats += seatNum;
                            } else {
                                seats += (", " + seatNum);
                            }
                        }
                        bookNumber.setText("Seat Number : " + seats);

                        for (MaterialButton btn : buttonList) {
                            if (!bookedSeats.containsKey(Integer.valueOf(String.valueOf(btn.getText())))) {
                                if (seatNumbers.contains(Integer.valueOf(String.valueOf(btn.getText())))) {
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(238, 153, 252)));
                                } else {
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                                }
                            }
                        }
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
