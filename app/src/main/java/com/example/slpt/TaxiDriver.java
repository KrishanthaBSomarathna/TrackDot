package com.example.slpt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slpt.SA22410122.RequestList1;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TaxiDriver extends AppCompatActivity {
    ImageView driverLogoImageView;
    TextView messageTextView;
    Button useMyLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_driver);

        driverLogoImageView = findViewById(R.id.imageView6);
        messageTextView = findViewById(R.id.textView9);
        useMyLocationButton = findViewById(R.id.button2);

        useMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationEnabled()) {
                   // startRequestListsActivity();
                    startActivity(new Intent(TaxiDriver.this,RequestList1.class));
                } else {
                    requestLocationServices();
               }


            }
        });
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void requestLocationServices() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startRequestListsActivity();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(TaxiDriver.this, 1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error for now
                    }
                }
            }
        });
    }

    private void startRequestListsActivity() {
        Intent intent = new Intent(TaxiDriver.this, RequestsList.class);
        startActivity(intent);
    }

    private class RequestsList {
    }

    public class RequestList extends RecyclerView.Adapter<RequestList.ViewHolder> {
        public List<PassengerRequest> passengerRequests;
        private String driverName;

        public RequestList(List<PassengerRequest> passengerRequests, String driverName) {
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_request_item, parent, false);
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
                    DatabaseReference acceptedTripsRef = FirebaseDatabase.getInstance().getReference().child("Taxi Driver");
                    String tripId = acceptedTripsRef.push().getKey();
                    acceptedTripsRef.child(tripId).setValue(acceptedTrip);

                    // Remove this request from the list or mark it as accepted
                    passengerRequests.remove(position);
                    notifyItemRemoved(position);

                }
            });
        }

        @Override
        public int getItemCount() {
            return passengerRequests.size();
        }

        public class PassengerRequest {
            public int getName() {
                return 0;
            }

            public int getContactNumber() {
                return 0;
            }

            public int getCurrentLocation() {
                return 0;
            }

            public int getDestinationLocation() {
                return 0;
            }
        }

        private class AcceptedTrip {
            public AcceptedTrip(int name, String driverName, int currentLocation, int destinationLocation) {

            }
        }
    }
}
