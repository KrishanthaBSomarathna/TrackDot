package com.example.slpt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HotelViewModel extends ViewModel {
    
    // LiveData for the list of hotels
    private MutableLiveData<List<Hotel>> hotels;
    
    // LiveData for the selected hotel
    private MutableLiveData<Hotel> selectedHotel;
    
    public HotelViewModel() {
        hotels = new MutableLiveData<>();
        selectedHotel = new MutableLiveData<>();
        
        // Load sample data
        loadHotels();
    }
    
    // Get the list of hotels
    public LiveData<List<Hotel>> getHotels() {
        return hotels;
    }
    
    // Get the selected hotel
    public LiveData<Hotel> getSelectedHotel() {
        return selectedHotel;
    }
    
    // Set the selected hotel
    public void setSelectedHotel(Hotel hotel) {
        selectedHotel.setValue(hotel);
    }
    
    // Load sample hotel data
    private void loadHotels() {
        List<Hotel> hotelList = new ArrayList<>();
        
        hotelList.add(new Hotel("1", "Grand Plaza Hotel", "New York, NY", 
            "Luxury hotel in the heart of Manhattan with stunning city views.", 4.5, 
            "https://example.com/grand_plaza.jpg", 299.99));
            
        hotelList.add(new Hotel("2", "Seaside Resort", "Miami Beach, FL", 
            "Beachfront resort with private beach access and spa facilities.", 4.8, 
            "https://example.com/seaside_resort.jpg", 399.99));
            
        hotelList.add(new Hotel("3", "Mountain Lodge", "Denver, CO", 
            "Cozy mountain lodge with scenic views and outdoor activities.", 4.2, 
            "https://example.com/mountain_lodge.jpg", 199.99));
            
        hotelList.add(new Hotel("4", "Urban Boutique Hotel", "San Francisco, CA", 
            "Modern boutique hotel in the trendy Mission District.", 4.6, 
            "https://example.com/urban_boutique.jpg", 349.99));
            
        hotelList.add(new Hotel("5", "Historic Inn", "Boston, MA", 
            "Charming historic inn with traditional New England hospitality.", 4.4, 
            "https://example.com/historic_inn.jpg", 249.99));
        
        hotels.setValue(hotelList);
    }
    
    // Search hotels by name
    public void searchHotels(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadHotels(); // Show all hotels if search is empty
            return;
        }
        
        List<Hotel> allHotels = hotels.getValue();
        if (allHotels == null) return;
        
        List<Hotel> filteredHotels = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Hotel hotel : allHotels) {
            if (hotel.getName().toLowerCase().contains(lowerQuery) ||
                hotel.getLocation().toLowerCase().contains(lowerQuery)) {
                filteredHotels.add(hotel);
            }
        }
        
        hotels.setValue(filteredHotels);
    }
}