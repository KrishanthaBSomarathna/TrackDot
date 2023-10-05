package com.example.slpt.schedule;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slpt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableBus extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private BusAdapter busAdapter;
    private ArrayList<BusDriver> list;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_bus);
        checkBox = findViewById(R.id.checkBox);

        recyclerView = findViewById(R.id.buslist);
        database = FirebaseDatabase.getInstance().getReference("Bus Drivers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        busAdapter = new BusAdapter(this, list);
        recyclerView.setAdapter(busAdapter);

        // Retrieve the road number from the intent
        String roadNumber = getIntent().getStringExtra("routeNumber");



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list to update with the new data
                list.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BusDriver busDriver = dataSnapshot.getValue(BusDriver.class);

                    // Check if the busDriver's road number matches the searched road number
                    if (busDriver != null && busDriver.getRoadnumber() != null && busDriver.getRoadnumber().equals(roadNumber)&& busDriver.getStatus().equals("online")) {
                        list.add(busDriver);
                    }
                }
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database query errors here
            }
        });



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.clear();
                if (isChecked) {
                    list.clear();
                    // Handle the check button click (e.g., show a TextView)

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Clear the existing list to update with the new data
                            list.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BusDriver busDriver = dataSnapshot.getValue(BusDriver.class);

                                // Check if the busDriver's road number matches the searched road number
                                if (busDriver != null && busDriver.getRoadnumber() != null && busDriver.getRoadnumber().equals(roadNumber)) {
                                    list.add(busDriver);
                                }
                            }
                            busAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database query errors here
                        }
                    });


                } else {

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Clear the existing list to update with the new data
                            list.clear();


                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BusDriver busDriver = dataSnapshot.getValue(BusDriver.class);

                                // Check if the busDriver's road number matches the searched road number
                                if (busDriver != null && busDriver.getRoadnumber() != null && busDriver.getRoadnumber().equals(roadNumber)&& busDriver.getStatus().equals("online")) {
                                    list.add(busDriver);
                                }
                            }
                            busAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database query errors here
                        }
                    });

                }
            }
        });





    }
}
