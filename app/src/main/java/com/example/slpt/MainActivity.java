package com.example.slpt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG_HOTEL_LIST = "hotel_list";
    private static final String TAG_HOTEL_DETAILS = "hotel_details";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Show hotel list fragment by default
        if (savedInstanceState == null) {
            showHotelList();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_hotel_list) {
            showHotelList();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void showHotelList() {
        // Check if fragment already exists
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(TAG_HOTEL_LIST);
        
        if (existingFragment == null) {
            // Create new fragment
            HotelListFragment fragment = new HotelListFragment();
            replaceFragment(fragment, TAG_HOTEL_LIST);
        } else {
            // Show existing fragment
            showFragment(existingFragment);
        }
        
        // Update action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hotel Locator");
        }
    }
    
    public void showHotelDetails() {
        // Check if fragment already exists
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(TAG_HOTEL_DETAILS);
        
        if (existingFragment == null) {
            // Create new fragment
            HotelDetailsFragment fragment = new HotelDetailsFragment();
            replaceFragment(fragment, TAG_HOTEL_DETAILS);
        } else {
            // Show existing fragment
            showFragment(existingFragment);
        }
        
        // Update action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hotel Details");
        }
    }
    
    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Replace the current fragment
        transaction.replace(R.id.fragment_container, fragment, tag);
        
        // Add to back stack for navigation
        transaction.addToBackStack(tag);
        
        // Commit the transaction
        transaction.commit();
    }
    
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Hide all fragments
        for (Fragment f : fragmentManager.getFragments()) {
            if (f != null) {
                transaction.hide(f);
            }
        }
        
        // Show the target fragment
        transaction.show(fragment);
        
        // Commit the transaction
        transaction.commit();
    }
    
    @Override
    public void onBackPressed() {
        // Handle back navigation
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}