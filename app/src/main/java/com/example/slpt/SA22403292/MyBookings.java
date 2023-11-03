package com.example.slpt.SA22403292;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.slpt.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyBookings extends AppCompatActivity implements MyBookingListAdapter.OnBookingDeleteListener {

    Dialog loadingView;
    private TabLayout tabLayout;
    private ListView prevBookingList;
    private ListView nextBookingList;

    private String userId = "+94761231234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        try {
            userId = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim();
        } catch (Exception ignore) {}

        loadingView = new Dialog(this);
        loadingView.setContentView(R.layout.loading_model_layout);
        loadingView.setCancelable(false);
        loadingView.show();

        tabLayout = findViewById(R.id.tabLayout);
        prevBookingList = findViewById(R.id.prevBookingList);
        nextBookingList = findViewById(R.id.nextBookingList);

        tabLayout.addTab(tabLayout.newTab().setText("Previous Bookings"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming Bookings"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    prevBookingList.setVisibility(View.VISIBLE);
                    nextBookingList.setVisibility(View.GONE);
                } else if (position == 1) {
                    prevBookingList.setVisibility(View.GONE);
                    nextBookingList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        loadData();
    }

    private void loadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Read all bookings
        databaseReference.child("Bus-Reservation").get().addOnCompleteListener(result -> {
            if (result.getException() != null) {
                loadingView.dismiss();
                Log.e("ERROR", "Error: ", result.getException());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("An error occurred while retrieving data. Please try again.");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Map<String,String> allBusses = new HashMap<>();
                // Read all bus data
                databaseReference.child("Route").get().addOnCompleteListener(busList -> {
                    for (DataSnapshot snapshot : busList.getResult().getChildren()) {
                        String indexOneStr = "";
                        for (DataSnapshot childSnap : snapshot.getChildren()) {
                            if (childSnap.getKey() != null && childSnap.getKey().equals("0")) {
                                indexOneStr = childSnap.getValue(String.class);
                                break;
                            }
                        }
                        allBusses.put(snapshot.getKey(), indexOneStr);
                    }

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        Date now = new Date();
                        List<BookingListItem> previousList = new ArrayList<>();
                        List<BookingListItem> nextList = new ArrayList<>();
                        for (DataSnapshot snapshot : result.getResult().getChildren()) {
                            String routeCode = snapshot.getKey();
                            for (DataSnapshot childSnapRoute : snapshot.getChildren()) {
                                String tripCode = childSnapRoute.getKey();
                                for (DataSnapshot childSnapDate : childSnapRoute.getChildren()) {
                                    String bookedDate = childSnapDate.getKey();
                                    for (DataSnapshot childSnap : childSnapDate.getChildren()) {
                                        String bookedNumber = childSnap.getValue(String.class);
                                        if (bookedNumber != null && bookedNumber.equals(userId)) {
                                            BookingListItem bookingListItem = new BookingListItem();
                                            bookingListItem.setCreatedDate(bookedDate);
                                            bookingListItem.setRouteCode(routeCode);
                                            bookingListItem.setTripCode(tripCode);
                                            bookingListItem.setSeatNumber(childSnap.getKey());
                                            if (tripCode.equals("1")) {
                                                bookingListItem.setRouteLabel(allBusses.get(routeCode));
                                            } else {
                                                bookingListItem.setRouteLabel(allBusses.get(routeCode) + " (reversed)");
                                            }
                                            if (format.parse(bookedDate).before(now)) {
                                                bookingListItem.setAllowEdit(false);
                                                previousList.add(bookingListItem);
                                            } else {
                                                bookingListItem.setAllowEdit(true);
                                                nextList.add(bookingListItem);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        loadingView.dismiss();
                        prevBookingList.setAdapter(new MyBookingListAdapter(this, previousList, databaseReference, this));
                        nextBookingList.setAdapter(new MyBookingListAdapter(this, nextList, databaseReference, this));
                    } catch (ParseException e) {
                        loadingView.dismiss();
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    @Override
    public void onBookingDeleted() {
        loadingView.show();
        loadData();
    }

}
