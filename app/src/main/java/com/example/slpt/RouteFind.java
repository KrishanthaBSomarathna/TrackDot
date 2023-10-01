package com.example.slpt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private EditText editText1, editText2;
    private Button searchButton;
    private RecyclerView recyclerView;

    private List<Route> matchingRoutes;
    private RouteAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_find);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        matchingRoutes = new ArrayList<>();
        // Pass the context (this) when creating the adapter
        adapter = new RouteAdapter(this, matchingRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Route");

        // When the search button is clicked, perform the search
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String value1 = editText1.getText().toString().trim();
                final String value2 = editText2.getText().toString().trim();

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
        });
    }
}
