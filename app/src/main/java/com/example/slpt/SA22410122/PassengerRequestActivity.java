package com.example.slpt.SA22410122;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

public class PassengerRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestListAdapter adapter;

    private DatabaseReference requestsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_request);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RequestListAdapter();
        recyclerView.setAdapter(adapter);

        requestsRef = FirebaseDatabase.getInstance().getReference().child("Passenger");

        // Load and display passenger requests
        loadRequests();
    }

    private void loadRequests() {
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PassengerRequest> requests = new ArrayList<>();

                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    PassengerRequest request = requestSnapshot.getValue(PassengerRequest.class);
                    if (request != null) {
                        requests.add(request);
                    }
                }

                adapter.setRequests(requests);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors, if necessary.
            }
        });
    }

    public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.RequestViewHolder> {
        private List<PassengerRequest> requests;

        public RequestListAdapter() {
            requests = new ArrayList<>();
        }

        public void setRequests(List<PassengerRequest> requests) {
            this.requests = requests;
        }

        @NonNull
        @Override
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_request, parent, false);
            return new RequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
            PassengerRequest request = requests.get(position);

            holder.nameTextView.setText("Name: " + request.getName());
            holder.contactNumberTextView.setText("Contact Number: " + request.getContactNumber());
            holder.currentLocationTextView.setText("Current Location: " + request.getCurrentLocation());
            holder.destinationLocationTextView.setText("Destination Location: " + request.getDestinationLocation());

            holder.confirmTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the "Confirm Trip" button click here
                    // You can start the LiveTrackActivity here
                    Intent intent = new Intent(PassengerRequestActivity.this, LiveTrack.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }

        public class RequestViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView contactNumberTextView;
            public TextView currentLocationTextView;
            public TextView destinationLocationTextView;
            public Button confirmTripButton;

            public RequestViewHolder(View itemView) {
                super(itemView);

                nameTextView = itemView.findViewById(R.id.nameTextView);
                contactNumberTextView = itemView.findViewById(R.id.contactNumberTextView);
                currentLocationTextView = itemView.findViewById(R.id.currentLocationTextView);
                destinationLocationTextView = itemView.findViewById(R.id.destinationLocationTextView);
                confirmTripButton = itemView.findViewById(R.id.confirmTripButton);
            }
        }
    }

    public static class PassengerRequest {
        private String name;
        private String contactNumber;
        private String currentLocation;
        private String destinationLocation;

        public PassengerRequest() {
            // Default constructor required for Firebase
        }

        public PassengerRequest(String name, String contactNumber, String currentLocation, String destinationLocation) {
            this.name = name;
            this.contactNumber = contactNumber;
            this.currentLocation = currentLocation;
            this.destinationLocation = destinationLocation;
        }

        public String getName() {
            return name;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public String getDestinationLocation() {
            return destinationLocation;
        }
    }
}
