package com.example.slpt.SA22403292;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.LoginHandalor;
import com.example.slpt.R;
import com.example.slpt.SA22403810.BusDriverView;
import com.example.slpt.SA22403810.SplashScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketBookInitializer extends AppCompatActivity implements SelectLocationDialog.OnItemSelectedListener {

    Dialog loadingView;
    private TextView startText;
    private TextView destinationText;

    private ListView numbersListView;

    private Map<String, List<String>> allBusses = new HashMap<>();
    private List<String> allStops = new ArrayList<>();
    private List<String> validDestinations = new ArrayList<>();
    private final float firstStopPrice = 100;
    private final float eachStopPrice = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set UI layout for activity
        setContentView(R.layout.activity_ticket_book_initializer);

        loadingView = new Dialog(this);
        loadingView.setContentView(R.layout.loading_model_layout);
        loadingView.setCancelable(false);
        loadingView.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        startText = findViewById(R.id.startText);
        destinationText = findViewById(R.id.endText);
        numbersListView = findViewById(R.id.busList);

        // Go to my bookings page. Used lambda expression to be more concise
        findViewById(R.id.my_booking_btn).setOnClickListener(
                view -> startActivity(new Intent(this, MyBookings.class))
        );

        startText.setEnabled(false);
        startText.setAlpha(0.3F);
        destinationText.setEnabled(false);
        destinationText.setAlpha(0.3F);

        allBusses = new HashMap<>();
        allStops = new ArrayList<>();
        validDestinations = new ArrayList<>();
        // Access firebase to retrieve data
        databaseReference.child("Route").get().addOnCompleteListener(result -> {
            loadingView.dismiss();
            // Check if an exception exist in firebase API call
            if (result.getException() != null) {
                Log.e("ERROR", "Error: ", result.getException());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("An error occurred while retrieving data. Please try again.");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Read bus route details
                for (DataSnapshot snapshot : result.getResult().getChildren()) {
                    List<String> busStops = new ArrayList<>();
                    String indexOneStr = "";
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        // Skip 0 to ignore the label
                        if (childSnap.getKey() != null && !childSnap.getKey().equals("0")) {
                            String busStop = childSnap.getValue(String.class);
                            busStops.add(busStop);
                            if (!allStops.contains(busStop)) {
                                allStops.add(busStop);
                            }
                        } else if (childSnap.getKey() != null) {
                            // Bus label
                            indexOneStr = childSnap.getValue(String.class);
                        }
                    }
                    busStops.add(0, indexOneStr);
                    allBusses.put(snapshot.getKey(), busStops);
                }

                startText.setEnabled(true);
                startText.setAlpha(1);

                startText.setOnClickListener(v -> {
                    // Open a filterable dialog
                    SelectLocationDialog dialogFragment = SelectLocationDialog.newInstance(allStops, "Select Start");
                    dialogFragment.show(getSupportFragmentManager(), "CustomSpinnerDialogFragmentStart");
                });
            }
        });
    }

    @Override
    public void onItemSelected(String selectedValue, boolean isStart) {
        if (isStart) {
            startText.setText(selectedValue);
            // Clear destination and list
            destinationText.setText("");
            numbersListView.setAdapter(new BusRouteListAdapter(this, new ArrayList<>()));
            destinationText.setEnabled(true);
            destinationText.setAlpha(1);
            validDestinations = new ArrayList<>();
            for (String key : allBusses.keySet()) {
                boolean flag = false;
                if (allBusses.get(key) == null) {
                    continue;
                }
                for (String valueStr : allBusses.get(key)) {
                    if (valueStr.equals(selectedValue)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    List<String> stations = allBusses.get(key);
                    for (int x = 1; x < stations.size(); x++) {
                        if (!validDestinations.contains(stations.get(x)) && !selectedValue.equals(stations.get(x))) {
                            validDestinations.add(stations.get(x));
                        }
                    }
                }
            }
            destinationText.setOnClickListener(v -> {
                SelectLocationDialog dialogFragment = SelectLocationDialog.newInstance(validDestinations, "Select Destination");
                dialogFragment.show(getSupportFragmentManager(), "CustomSpinnerDialogFragmentDest");
            });
        } else {
            destinationText.setText(selectedValue);
            numbersListView.setAdapter(new BusRouteListAdapter(this, new ArrayList<>()));
            Map<String, BusListItem> eligibleBusses = new HashMap<>();
            String start = (String) startText.getText();
            // loop every bus to get busses going through both start and destination
            for (String key : allBusses.keySet()) {
                boolean isStartToEnd = false;
                boolean flagStart = false;
                boolean flagDest = false;
                if (allBusses.get(key) == null) {
                    continue;
                }
                float stopCount = 0;
                for (String valueStr : allBusses.get(key)) {
                    if (valueStr.equals(start)) {
                        flagStart = true;
                        if (flagDest) {
                            isStartToEnd = false;
                        }
                    }
                    if (valueStr.equals(selectedValue)) {
                        flagDest = true;
                        if (flagStart) {
                            isStartToEnd = true;
                        }
                    }
                    if (flagStart || flagDest) {
                        stopCount += 1;
                    }
                    if (flagStart && flagDest) {
                        break;
                    }
                }
                // Add only eligible busses
                if (flagStart && flagDest && !eligibleBusses.containsKey(key)) {
                    float price = firstStopPrice;
                    if (stopCount > 1) {
                        price += ((stopCount - 1) * eachStopPrice);
                    }
                    if (isStartToEnd) {
                        eligibleBusses.put(key, new BusListItem(true, price));
                    } else {
                        eligibleBusses.put(key, new BusListItem(false, price));
                    }
                }
            }
            // Set data to a custom object for display and navigation
            List<BusListItem> busses = new ArrayList<>();
            for (String key : eligibleBusses.keySet()) {
                BusListItem bus = new BusListItem();
                bus.setRouteNumber(key);
                if (allBusses.get(key) != null) {
                    if (eligibleBusses.get(key).isStartToEnd()) {
                        bus.setRouteDesc(allBusses.get(key).get(0));
                        bus.setStartToEnd(true);
                    } else {
                        bus.setRouteDesc(allBusses.get(key).get(0) + " (reverse)");
                        bus.setStartToEnd(false);
                    }
                } else {
                    bus.setRouteDesc("N/A");
                }
                bus.setStartStation(start);
                bus.setEndStation(selectedValue);
                bus.setPricePerSeat(eligibleBusses.get(key).getPricePerSeat());
                busses.add(bus);
            }
            BusRouteListAdapter numbersArrayAdapter = new BusRouteListAdapter(this, busses);
            numbersListView.setAdapter(numbersArrayAdapter);
        }
    }

}
