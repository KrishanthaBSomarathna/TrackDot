package com.example.slpt.SA22403292;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.slpt.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MyBookingListAdapter extends ArrayAdapter<BookingListItem> {

    private final List<BookingListItem> bookingList;
    private final Context context;
    private final DatabaseReference dbReference;

    private final OnBookingDeleteListener onBookingDeleteListener;

    public interface OnBookingDeleteListener {
        void onBookingDeleted();
    }

    public MyBookingListAdapter(Context context, List<BookingListItem> bookingList, DatabaseReference dbReference, OnBookingDeleteListener onBookingDeleteListener) {
        super(context, 0, bookingList);
        this.context = context;
        this.dbReference = dbReference;
        this.bookingList = bookingList;
        this.onBookingDeleteListener = onBookingDeleteListener;
    }

    @Override
    public int getCount() {
        return bookingList.size();
    }

    @Override
    public BookingListItem getItem(int position) {
        return bookingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.booking_list_layout, parent, false);
        }

        BookingListItem bookingItem = bookingList.get(position);

        ((TextView) view.findViewById(R.id.booked_date)).setText(bookingItem.getCreatedDate().replaceAll("-", "/"));
        ((TextView) view.findViewById(R.id.bus_route)).setText("Seat " + bookingItem.getSeatNumber() + " on " + bookingItem.getRouteCode());
        ((TextView) view.findViewById(R.id.bus_route_desc)).setText(bookingItem.getRouteLabel());

        Button button = view.findViewById(R.id.delete_booking);

        if (bookingItem.isAllowEdit()) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }

        if (bookingItem.isAllowEdit()) {
            button.setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setTitle("Delete Confirmation")
                        .setPositiveButton("Cancel", (dialog, id) -> {
                            dialog.dismiss();
                        })
                        .setNegativeButton("Delete", (dialog, id) -> {
                            dbReference.child("Bus-Reservation")
                                    .child(bookingItem.getRouteCode())
                                    .child(bookingItem.getTripCode())
                                    .child(bookingItem.getCreatedDate())
                                    .child(bookingItem.getSeatNumber())
                                    .removeValue().addOnCompleteListener(value -> {
                                        Toast.makeText(context, "Booking Removed Successfully.", Toast.LENGTH_SHORT).show();
                                        onBookingDeleteListener.onBookingDeleted();
                                    });
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }

        return view;
    }

}
