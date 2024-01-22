package com.example.slpt.SA22403810;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slpt.R;
import com.example.slpt.SA22403292.PassengerTicketBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BusDriver> list;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double userLatitude;
    private double userLongitude;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Passenger");
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public BusAdapter(Context context, ArrayList<BusDriver> list) {
        this.context = context;
        this.list = list;
        initLocationManager();
        startLocationUpdates();
    }

    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();

                // Calculate distances and sort the buses
                List<BusDriver> buses = new ArrayList<>(list);
                Collections.sort(buses, (bus1, bus2) ->
                        Double.compare(calculateDistance(userLatitude, userLongitude, bus1.getLatitude(), bus1.getLongitude()),
                                calculateDistance(userLatitude, userLongitude, bus2.getLatitude(), bus2.getLongitude())));

                // Update the adapter with the sorted list
                updateBusList(buses);

                // Display toast message for distance update
//                Toast.makeText(context, "Distance updated based on your location", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bus_driver_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BusDriver busDriver = list.get(position);

        holder.LocationName.setText(busDriver.getLocationName());
        holder.bustype.setText(busDriver.getBustype());
        holder.roadnumber.setText(busDriver.getRoadnumber());
        holder.busnumber.setText(busDriver.getVehicleNum());
        holder.currentroute.setText(busDriver.getCurrentroute());

        String status = busDriver.getStatus().toString();


        String vehiclenumber = busDriver.getVehicleNum().toString();
        String route = busDriver.getRoadnumber();
        Double latitude = busDriver.getLatitude();
        Double longitude = busDriver.getLongitude();

        // Calculate distance between user and bus
        double distance = calculateDistance(userLatitude, userLongitude, latitude, longitude);

        // Display distance in kilometers
        String distanceText = String.format("%.2f km", distance);
        holder.distanceTextView.setText(distanceText);


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference savedBusRef = databaseReference.child(firebaseUser.getPhoneNumber().toString()).child("savedbus");

                savedBusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> savedBuses = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot busSnapshot : dataSnapshot.getChildren()) {
                                String bus = busSnapshot.getValue(String.class);
                                if (bus != null) {
                                    savedBuses.add(bus);
                                }
                            }
                        }

                        if (isChecked) {
                            // Checkbox is checked
                            String busToAdd = busDriver.getVehicleNum().toString();
                            if (!savedBuses.contains(busToAdd)) {
                                // Add the new saved bus
                                savedBuses.add(busToAdd);
                            }
                        } else {
                            // Checkbox is unchecked
                            String busToRemove = busDriver.getVehicleNum().toString();
                            savedBuses.remove(busToRemove);
                        }

                        // Update the saved buses array in Firebase
                        savedBusRef.setValue(savedBuses);

                        // Set "saved" based on whether the array is empty or not
                        boolean isSaved = !savedBuses.isEmpty();
                        databaseReference.child(firebaseUser.getPhoneNumber().toString()).child("saved").setValue(isSaved ? "true" : "false");

                        // Show the appropriate toast message
                        if (isChecked) {
                            Toast.makeText(context, "Bus Saved", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Bus Unsaved", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });

        String className = context.getClass().getSimpleName();

        if (className.equals("LiveRadars")) {
            holder.savebtn.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the vehicle number of the bus to delete
                String busToRemove = busDriver.getVehicleNum().toString();

                // Reference to the "savedbus" node
                DatabaseReference savedBusRef = databaseReference.child(firebaseUser.getPhoneNumber().toString()).child("savedbus");

                savedBusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> savedBuses = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot busSnapshot : dataSnapshot.getChildren()) {
                                String bus = busSnapshot.getValue(String.class);
                                if (bus != null) {
                                    savedBuses.add(bus);
                                }
                            }

                            // Remove the bus to delete from the savedBuses list
                            savedBuses.remove(busToRemove);

                            // Update the saved buses array in Firebase
                            savedBusRef.setValue(savedBuses);

                            // Notify the adapter that an item has been removed
                            list.remove(position);
                            notifyItemRemoved(position);

                            // Notify the adapter that the data range has changed
                            notifyItemRangeChanged(position, list.size());

                            // Show a toast message for deletion
                            Toast.makeText(context, "Bus Deleted", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });

        holder.booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PassengerTicketBook.class);
                intent.putExtra("vehiclenumber", vehiclenumber);
                intent.putExtra("route", route);
                context.startActivity(intent);
            }
        });

        holder.showinmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("offline")) {
                    Toast.makeText(context.getApplicationContext(), "Bus Driver Not Available", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(context, BusLocation.class);
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lon", longitude);
                    intent.putExtra("vehiclenumber", vehiclenumber);
                    context.startActivity(intent);
                }
            }
        });

        if (status.equals("online")) {
            holder.online.setVisibility(View.VISIBLE);
            holder.offline.setVisibility(View.GONE);
        } else {
            holder.offline.setVisibility(View.VISIBLE);
            holder.online.setVisibility(View.GONE);
        }
    }

    private double calculateDistance(double userLat, double userLon, double busLat, double busLon) {
        // Calculate distance using Haversine formula
        double earthRadius = 6371; // Radius of Earth in kilometers
        double dLat = Math.toRadians(busLat - userLat);
        double dLon = Math.toRadians(busLon - userLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(busLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // Distance in kilometers
    }

    private void updateBusList(List<BusDriver> updatedList) {
        list.clear();
        list.addAll(updatedList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView currentroute,showinmap, booknow, LocationName, bustype, roadnumber, busnumber, r1starttime, r1stoptime, r2starttime, r2stoptime, r1startpoint, r1stoppoint, r2startpoint, r2stoppoint, online, offline, delete;
        LinearLayout savebtn;
        CheckBox checkBox;
        TextView distanceTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            showinmap = itemView.findViewById(R.id.buslocation);
            LocationName = itemView.findViewById(R.id.location);
            bustype = itemView.findViewById(R.id.bustype);
            roadnumber = itemView.findViewById(R.id.roadNumber);
            busnumber = itemView.findViewById(R.id.busnumber);

            currentroute = itemView.findViewById(R.id.currentroute);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            delete = itemView.findViewById(R.id.delete);
            booknow = itemView.findViewById(R.id.booknow);
            checkBox = itemView.findViewById(R.id.checkBox);
            savebtn = itemView.findViewById(R.id.savebtn);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
        }
    }
}
