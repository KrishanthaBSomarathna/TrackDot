package com.example.slpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BusDriver> list;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView LocationName, bustype, roadnumber,busnumber,r1starttime,r1stoptime,r2starttime,r2stoptime,r1startpoint,r1stoppoint,r2startpoint,r2stoppoint;

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
        }
    }
}
