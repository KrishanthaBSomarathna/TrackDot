package com.example.slpt.SA22410122;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.slpt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequestList1 extends RecyclerView.Adapter<RequestList1.ViewHolder> {
    private List<PassengerRequest> passengerRequests;
    private String driverName;

    public RequestList1(List<PassengerRequest> passengerRequests, String driverName) {
        this.passengerRequests = passengerRequests;
        this.driverName = driverName;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView passengerNameTextView;
        public TextView contactNumberTextView;
        public TextView currentLocationTextView;
        public TextView destinationLocationTextView;
        public Button acceptTripButton;

        public ViewHolder(View itemView) {
            super(itemView);

            passengerNameTextView = itemView.findViewById(R.id.passengerNameTextView);
            contactNumberTextView = itemView.findViewById(R.id.contactNumberTextView);
            currentLocationTextView = itemView.findViewById(R.id.currentLocationTextView);
            destinationLocationTextView = itemView.findViewById(R.id.destinationLocationTextView);
            acceptTripButton = itemView.findViewById(R.id.acceptTripButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_request_list1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PassengerRequest request = passengerRequests.get(position);

        holder.passengerNameTextView.setText(request.getName());
        holder.contactNumberTextView.setText(request.getContactNumber());
        holder.currentLocationTextView.setText(request.getCurrentLocation());
        holder.destinationLocationTextView.setText(request.getDestinationLocation());

        holder.acceptTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "Confirm Trip" button click here

                // Get the selected passenger request
                PassengerRequest selectedRequest = passengerRequests.get(position);

                // Create an AcceptedTrip object
                AcceptedTrip acceptedTrip = new AcceptedTrip(
                        selectedRequest.getName(),
                        driverName,
                        selectedRequest.getCurrentLocation(),
                        selectedRequest.getDestinationLocation()
                );

                // Save the accepted trip to Firebase
                DatabaseReference acceptedTripsRef = FirebaseDatabase.getInstance().getReference().child("AcceptedTrips");
                String tripId = acceptedTripsRef.push().getKey();
                acceptedTripsRef.child(tripId).setValue(acceptedTrip);

                // Remove this request from the list
                passengerRequests.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passengerRequests.size();
    }

    public static class PassengerRequest {
        private String name;
        private String contactNumber;
        private String currentLocation;
        private String destinationLocation;

        public PassengerRequest() {
            // Default constructor required for Firebase
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

    public static class AcceptedTrip {
        private String passengerName;
        private String driverName;
        private String currentLocation;
        private String destinationLocation;

        public AcceptedTrip() {
            // Default constructor required for Firebase
        }

        public AcceptedTrip(String passengerName, String driverName, String currentLocation, String destinationLocation) {
            this.passengerName = passengerName;
            this.driverName = driverName;
            this.currentLocation = currentLocation;
            this.destinationLocation = destinationLocation;
        }
    }
}
