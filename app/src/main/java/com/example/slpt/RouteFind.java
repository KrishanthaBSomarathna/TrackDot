package com.example.slpt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RouteFind extends AppCompatActivity {


    private RecyclerView recyclerView;

    private List<Route> matchingRoutes;
    private RouteAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_find);

        recyclerView = findViewById(R.id.recyclerView);

        matchingRoutes = new ArrayList<>();
        // Pass the context (this) when creating the adapter
        adapter = new RouteAdapter(this, matchingRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Route");


        Intent intent = getIntent();
        String value1 = intent.getStringExtra("origin").toLowerCase().trim();
        String value2 = intent.getStringExtra("destination").toLowerCase().trim();

                // Read data from Firebase
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        matchingRoutes.clear();

                        // Iterate through the routes
                        for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                            Iterable<DataSnapshot> routeData = routeSnapshot.getChildren();
                            List<String> routeValues = new ArrayList<>();

                            // Iterate through the values in each route
                            for (DataSnapshot valueSnapshot : routeData) {
                                routeValues.add(valueSnapshot.getValue(String.class));
                            }

                            // Check if both values exist in the route
                            if (routeValues.contains(value1) && routeValues.contains(value2)) {
                                String routeNumber = routeSnapshot.getKey();
                                String routeDetails = "Details for Route " + routeValues.get(0).toString(); // You should retrieve actual details
                                matchingRoutes.add(new Route(routeNumber, routeDetails));
                            }
                        }

                        // Notify the adapter that data has changed
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors here
                    }
                });


    }
}
