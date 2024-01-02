package com.example.slpt.SA22403810;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.slpt.R;

import java.time.Instant;

public class BookBus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bus);

        Intent intent = getIntent();

        String vehicleNumber = intent.getStringExtra("vehiclenumber");
        String route = intent.getStringExtra("route");


    }
}