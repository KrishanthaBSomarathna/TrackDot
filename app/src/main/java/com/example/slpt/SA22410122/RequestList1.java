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
import com.example.slpt.TaxiDriver;

import java.util.List;

public class RequestList1 extends RecyclerView.Adapter<RequestList1.ViewHolder> {
    private List<TaxiDriver.RequestList.PassengerRequest> passengerRequests;
    private OnAcceptTripClickListener acceptTripClickListener;

    public interface OnAcceptTripClickListener {
        void onAcceptTripClick(int position, TaxiDriver.RequestList.PassengerRequest request);
    }

    public RequestList1(List<TaxiDriver.RequestList.PassengerRequest> passengerRequests, OnAcceptTripClickListener acceptTripClickListener) {
        this.passengerRequests = passengerRequests;
        this.acceptTripClickListener = acceptTripClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaxiDriver.RequestList.PassengerRequest request = passengerRequests.get(position);
        holder.passengerNameTextView.setText(request.getName());
        holder.contactNumberTextView.setText(request.getContactNumber());
        holder.currentLocationTextView.setText(request.getCurrentLocation());
        holder.destinationLocationTextView.setText(request.getDestinationLocation());

        holder.acceptTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acceptTripClickListener != null) {
                    acceptTripClickListener.onAcceptTripClick(position, request);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return passengerRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView passengerNameTextView;
        public TextView contactNumberTextView;
        public TextView currentLocationTextView;
        public TextView destinationLocationTextView;
        public Button acceptTripButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerNameTextView = itemView.findViewById(R.id.passengerNameTextView);
            contactNumberTextView = itemView.findViewById(R.id.contactNumberTextView);
            currentLocationTextView = itemView.findViewById(R.id.currentLocationTextView);
            destinationLocationTextView = itemView.findViewById(R.id.destinationLocationTextView);
            acceptTripButton = itemView.findViewById(R.id.acceptTripButton);
        }
    }
}