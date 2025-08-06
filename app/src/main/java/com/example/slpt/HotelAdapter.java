package com.example.slpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {
    
    private List<Hotel> hotels;
    private OnHotelClickListener listener;
    
    // Interface for handling hotel item clicks
    public interface OnHotelClickListener {
        void onHotelClick(Hotel hotel);
    }
    
    public HotelAdapter(OnHotelClickListener listener) {
        this.hotels = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.bind(hotel);
    }
    
    @Override
    public int getItemCount() {
        return hotels.size();
    }
    
    // Update the list of hotels
    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels != null ? hotels : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    // ViewHolder class
    class HotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotelImage;
        private TextView hotelName;
        private TextView hotelLocation;
        private TextView hotelPrice;
        private RatingBar hotelRating;
        
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            
            hotelImage = itemView.findViewById(R.id.hotel_image);
            hotelName = itemView.findViewById(R.id.hotel_name);
            hotelLocation = itemView.findViewById(R.id.hotel_location);
            hotelPrice = itemView.findViewById(R.id.hotel_price);
            hotelRating = itemView.findViewById(R.id.hotel_rating);
            
            // Set click listener for the entire item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onHotelClick(hotels.get(position));
                }
            });
        }
        
        public void bind(Hotel hotel) {
            hotelName.setText(hotel.getName());
            hotelLocation.setText(hotel.getLocation());
            hotelPrice.setText(String.format("$%.2f/night", hotel.getPrice()));
            hotelRating.setRating((float) hotel.getRating());
            
            // Load hotel image using Glide
            if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(hotel.getImageUrl())
                        .placeholder(R.drawable.hotel_placeholder)
                        .error(R.drawable.hotel_placeholder)
                        .into(hotelImage);
            } else {
                hotelImage.setImageResource(R.drawable.hotel_placeholder);
            }
        }
    }
}