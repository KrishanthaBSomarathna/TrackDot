package com.example.slpt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private List<Route> routeList;
    private Context context;

    public RouteAdapter(Context context,List<Route> routeList) {
        this.routeList = routeList;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Route route = routeList.get(position);

        holder.routeNumberTextView.setText("Route Number: " + route.getRouteNumber());
        holder.routeDetailsTextView.setText(route.getRouteDetails());

        // Set an OnClickListener for the CardView item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display a Toast with the route number
                Toast.makeText(view.getContext(), "Route Number: " + route.getRouteNumber(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AvailableBus.class);
                intent.putExtra("routeNumber", route.getRouteNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView routeNumberTextView;
        TextView routeDetailsTextView;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeNumberTextView = itemView.findViewById(R.id.routeNumberTextView);
            routeDetailsTextView = itemView.findViewById(R.id.routeDetailsTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
