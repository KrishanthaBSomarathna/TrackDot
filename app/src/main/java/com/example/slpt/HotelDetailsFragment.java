package com.example.slpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

public class HotelDetailsFragment extends Fragment {
    
    private HotelViewModel hotelViewModel;
    private ImageView hotelImage;
    private TextView hotelName;
    private TextView hotelLocation;
    private TextView hotelDescription;
    private TextView hotelPrice;
    private RatingBar hotelRating;
    private Button backButton;
    private Button bookButton;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_details, container, false);
        
        // Initialize views
        hotelImage = view.findViewById(R.id.hotel_image_details);
        hotelName = view.findViewById(R.id.hotel_name_details);
        hotelLocation = view.findViewById(R.id.hotel_location_details);
        hotelDescription = view.findViewById(R.id.hotel_description_details);
        hotelPrice = view.findViewById(R.id.hotel_price_details);
        hotelRating = view.findViewById(R.id.hotel_rating_details);
        backButton = view.findViewById(R.id.button_back);
        bookButton = view.findViewById(R.id.button_book);
        
        // Initialize ViewModel
        hotelViewModel = new ViewModelProvider(requireActivity()).get(HotelViewModel.class);
        
        // Setup click listeners
        setupClickListeners();
        
        // Observe selected hotel
        observeSelectedHotel();
        
        return view;
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHotelList();
            }
        });
        
        bookButton.setOnClickListener(v -> {
            // Handle booking logic here
            if (hotelViewModel.getSelectedHotel().getValue() != null) {
                // You could show a booking dialog or navigate to booking activity
                showBookingConfirmation();
            }
        });
    }
    
    private void observeSelectedHotel() {
        hotelViewModel.getSelectedHotel().observe(getViewLifecycleOwner(), hotel -> {
            if (hotel != null) {
                displayHotelDetails(hotel);
            }
        });
    }
    
    private void displayHotelDetails(Hotel hotel) {
        hotelName.setText(hotel.getName());
        hotelLocation.setText(hotel.getLocation());
        hotelDescription.setText(hotel.getDescription());
        hotelPrice.setText(String.format("$%.2f per night", hotel.getPrice()));
        hotelRating.setRating((float) hotel.getRating());
        
        // Load hotel image using Glide
        if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(hotel.getImageUrl())
                    .placeholder(R.drawable.hotel_placeholder)
                    .error(R.drawable.hotel_placeholder)
                    .into(hotelImage);
        } else {
            hotelImage.setImageResource(R.drawable.hotel_placeholder);
        }
    }
    
    private void showBookingConfirmation() {
        // Simple booking confirmation - in a real app, you'd show a dialog or navigate to booking screen
        if (getActivity() != null) {
            android.widget.Toast.makeText(getActivity(), 
                "Booking request sent for " + hotelViewModel.getSelectedHotel().getValue().getName(), 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}