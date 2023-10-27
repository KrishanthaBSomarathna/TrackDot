package com.example.slpt.SA22403810;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slpt.SA22403292.PassengerTicketBook;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BusDriver> list;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Passenger");
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public BusAdapter(Context context, ArrayList<BusDriver> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bus_driver_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BusDriver busDriver = list.get(position);
        holder.LocationName.setText(busDriver.getLocationName());
        holder.bustype.setText(busDriver.getBustype());
        holder.roadnumber.setText(busDriver.getRoadnumber());
        holder.busnumber.setText(busDriver.getVehicleNum());
        holder.r1starttime.setText(busDriver.getR1start());
        holder.r1stoptime.setText(busDriver.getR1stop());
        holder.r2starttime.setText(busDriver.getR2start());
        holder.r2stoptime.setText(busDriver.getR2stop());
        holder.r2startpoint.setText(busDriver.getStopdestination());
        holder.r2stoppoint.setText(busDriver.getStartdestination());
        holder.r1startpoint.setText(busDriver.getStartdestination());
        holder.r1stoppoint.setText(busDriver.getStopdestination());
        String status = busDriver.getStatus().toString();
        String vehiclenumber = busDriver.getVehicleNum().toString();
        String route = busDriver.getRoadnumber();
        Double latitude = busDriver.getLatitude();
        Double longitude = busDriver.getLongitude();

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference savedBusRef = databaseReference.child("787175969").child("savedbus");

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
                        databaseReference.child("787175969").child("saved").setValue(isSaved ? "true" : "false");

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

        // Check if the current context is an instance of LiveRadars
        if (className.equals("LiveRadars")) {
            holder.savebtn.setVisibility(View.GONE);
        }
        else {
            holder.delete.setVisibility(View.GONE);

        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the vehicle number of the bus to delete
                String busToRemove = busDriver.getVehicleNum().toString();

                // Reference to the "savedbus" node
                DatabaseReference savedBusRef = databaseReference.child("787175969").child("savedbus");

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
                Intent intent = new Intent(context, BusLocation.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                context.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView showinmap,booknow, LocationName, bustype, roadnumber, busnumber, r1starttime, r1stoptime, r2starttime, r2stoptime, r1startpoint, r1stoppoint, r2startpoint, r2stoppoint, online, offline, delete;
        LinearLayout savebtn;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            showinmap = itemView.findViewById(R.id.buslocation);
            LocationName = itemView.findViewById(R.id.location);
            bustype = itemView.findViewById(R.id.bustype);
            roadnumber = itemView.findViewById(R.id.roadNumber);
            busnumber = itemView.findViewById(R.id.busnumber);
            r1starttime = itemView.findViewById(R.id.r1starttime);
            r1stoptime = itemView.findViewById(R.id.r1stopttime);
            r2starttime = itemView.findViewById(R.id.r2starttime);
            r2stoptime = itemView.findViewById(R.id.r2stopttime);

            r1startpoint = itemView.findViewById(R.id.r1startpoint);
            r1stoppoint = itemView.findViewById(R.id.r1stoppoint);
            r2startpoint = itemView.findViewById(R.id.r2startpoint);
            r2stoppoint = itemView.findViewById(R.id.r2stoppoint);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            delete = itemView.findViewById(R.id.delete);
            booknow = itemView.findViewById(R.id.booknow);
            checkBox = itemView.findViewById(R.id.checkBox);
            savebtn = itemView.findViewById(R.id.savebtn);
        }
    }
}
