package com.example.slpt.SA22403810;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LiveRadars extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private BusAdapter busAdapter;
    private ArrayList<BusDriver> list;

    TextView goToRoute;

    ArrayList<String> savedBuses; // List to store saved bus numbers

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_radars);

        goToRoute = findViewById(R.id.goToRoute);
        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        busAdapter = new BusAdapter(this, list);
        recyclerView.setAdapter(busAdapter);


        goToRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RouteFinder.class);
                startActivity(intent);
                Animatoo.INSTANCE.animateShrink(LiveRadars.this);
            }
        });

        database.child("Passenger").child(firebaseUser.getPhoneNumber().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String savedValue = snapshot.child("saved").getValue(String.class);
                if (savedValue != null && savedValue.equals("true")) {
                    savedBuses = new ArrayList<>(); // Initialize the list for saved bus numbers
                    DataSnapshot savedBusSnapshot = snapshot.child("savedbus");
                    for (DataSnapshot busNumberSnapshot : savedBusSnapshot.getChildren()) {
                        String busNumber = busNumberSnapshot.getValue(String.class);
                        if (busNumber != null) {
                            savedBuses.add(busNumber);
                        }
                    }
                    // Query the bus drivers that match the saved bus numbers
                    database.child("Bus Drivers").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Clear the existing list to update with the new data
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BusDriver busDriver = dataSnapshot.getValue(BusDriver.class);
                                if (busDriver != null && busDriver.getVehicleNum() != null && savedBuses.contains(busDriver.getVehicleNum())) {

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
                    // Toast a message indicating that there's no saved bus
                    Toast.makeText(getApplicationContext(), "No saved bus", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }
    public void onBackPressed() {
        // Handle back button press, navigate to home screen
        Intent intent = new Intent(getApplicationContext(),PassengerMainView.class);

        startActivity(intent);
        Animatoo.INSTANCE.animateSwipeRight(LiveRadars.this);
    }
}
