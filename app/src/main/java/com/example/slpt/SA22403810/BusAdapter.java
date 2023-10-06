package com.example.slpt.SA22403810;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slpt.SA22403292.PassengerTicketBook;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        holder.savebus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(String.valueOf(firebaseUser.getPhoneNumber())).child("savedbus").setValue(busDriver.getVehicleNum().toString());
                Toast.makeText(context,"Bus Saved",Toast.LENGTH_LONG).show();
            }
        });

        holder.booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, PassengerTicketBook.class);
                intent.putExtra("vehicalenumber",vehiclenumber);
                intent.putExtra("route",route);
                context.startActivity(intent);


            }
        });



        if (status.equals("online")){
            holder.online.setVisibility(View.VISIBLE);
            holder.offline.setVisibility(View.GONE);

        }
        else {
            holder.offline.setVisibility(View.VISIBLE);
            holder.online.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView booknow,LocationName, bustype, roadnumber,busnumber,r1starttime,r1stoptime,r2starttime,r2stoptime,r1startpoint,r1stoppoint,r2startpoint,r2stoppoint,online,offline
                ,savebus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            LocationName = itemView.findViewById(R.id.location);
            bustype = itemView.findViewById(R.id.bustype);
            roadnumber = itemView.findViewById(R.id.roadNumber);
            busnumber = itemView.findViewById(R.id.busnumber);
            r1starttime = itemView.findViewById(R.id.r1starttime);
            r1stoptime = itemView.findViewById(R.id.r1stopttime);
            r2starttime = itemView.findViewById(R.id.r2starttime);
            r2stoptime = itemView.findViewById(R.id.r2stopttime);

            r1startpoint = itemView.findViewById(R.id.r1startpoint);
            r1stoppoint  = itemView.findViewById(R.id.r1stoppoint);
            r2startpoint = itemView.findViewById(R.id.r2startpoint);
            r2stoppoint  = itemView.findViewById(R.id.r2stoppoint);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            savebus = itemView.findViewById(R.id.savebus);
            booknow = itemView.findViewById(R.id.booknow);
        }
    }
}
