package com.example.slpt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HotelListFragment extends Fragment implements HotelAdapter.OnHotelClickListener {
    
    private HotelViewModel hotelViewModel;
    private HotelAdapter hotelAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_hotels);
        searchEditText = view.findViewById(R.id.edit_text_search);
        
        // Initialize ViewModel
        hotelViewModel = new ViewModelProvider(requireActivity()).get(HotelViewModel.class);
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup search functionality
        setupSearch();
        
        // Observe hotel data
        observeHotels();
        
        return view;
    }
    
    private void setupRecyclerView() {
        hotelAdapter = new HotelAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(hotelAdapter);
    }
    
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                hotelViewModel.searchHotels(s.toString());
            }
        });
    }
    
    private void observeHotels() {
        hotelViewModel.getHotels().observe(getViewLifecycleOwner(), hotels -> {
            hotelAdapter.setHotels(hotels);
        });
    }
    
    @Override
    public void onHotelClick(Hotel hotel) {
        // Set the selected hotel in ViewModel
        hotelViewModel.setSelectedHotel(hotel);
        
        // Navigate to details fragment
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showHotelDetails();
        }
    }
}