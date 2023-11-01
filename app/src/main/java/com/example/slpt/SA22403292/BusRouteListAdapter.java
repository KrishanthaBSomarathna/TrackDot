package com.example.slpt.SA22403292;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.slpt.R;

import java.util.List;
import java.util.Locale;

public class BusRouteListAdapter extends ArrayAdapter<BusListItem> {

    private final List<BusListItem> busList;
    private final Context context;

    public BusRouteListAdapter(Context context, List<BusListItem> busList) {
        super(context, 0, busList);
        this.context = context;
        this.busList = busList;
    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public BusListItem getItem(int position) {
        return busList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.bus_route_list_layout, parent, false);
        }

        BusListItem bus = busList.get(position);

        TextView busRoute = view.findViewById(R.id.bus_route);
        busRoute.setText(bus.getRouteNumber());
        TextView busRouteName = view.findViewById(R.id.bus_route_desc);
        busRouteName.setText(bus.getRouteDesc());
        TextView pricePerSeat = view.findViewById(R.id.price_per_seat);
        pricePerSeat.setText("Rs " + String.format(Locale.getDefault(), "%.1f", bus.getPricePerSeat()));
        Button button = view.findViewById(R.id.go_to_book);
        button.setOnClickListener(btn -> {
            Intent intent = new Intent(context, PassengerTicketBook.class);
            intent.putExtra("start", bus.getStartStation());
            intent.putExtra("end", bus.getEndStation());
            intent.putExtra("route", bus.getRouteNumber());
            intent.putExtra("isStartToEnd", bus.isStartToEnd());
            intent.putExtra("pricePerSeat", bus.getPricePerSeat());
            context.startActivity(intent);
        });

        return view;
    }

}
