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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView LocationName, bustype, roadnumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            LocationName = itemView.findViewById(R.id.tvfirstName);
            bustype = itemView.findViewById(R.id.tvlastName);
            roadnumber = itemView.findViewById(R.id.tvage);
        }
    }
}
