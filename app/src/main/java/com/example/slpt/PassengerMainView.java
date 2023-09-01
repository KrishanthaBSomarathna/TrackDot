package com.example.slpt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PassengerMainView extends AppCompatActivity {
    private TextView clockTextView;
    private TextView dateTextView;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FIRST_TIME_KEY = "isFirstTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengermainview);
        clockTextView = findViewById(R.id.clockTextView);
        dateTextView = findViewById(R.id.dateTextView);
        updateClockAndDate();



        // Check if the app is being launched for the first time
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME_KEY, true);

        if (isFirstTime) {
            // Set the flag indicating that the app has been launched at least once
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRST_TIME_KEY, false);
            editor.apply();

            // Open the guide or welcome activity
            startActivity(new Intent(PassengerMainView.this, PassengerGuide.class));
        }



    }
    public void onBackPressed() {
        // Handle back button press, navigate to home screen
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateClockAndDate() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());

        String currentTime = timeFormat.format(new Date());
        String currentDate = dateFormat.format(new Date());

        clockTextView.setText(currentTime);
        dateTextView.setText(currentDate);
    }



}