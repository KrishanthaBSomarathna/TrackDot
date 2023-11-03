package com.example.slpt.SA22403742;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slpt.R;

public class Homepage extends AppCompatActivity {

    private Button button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize the buttons by finding them in the XML layout
        button2 = findViewById(R.id.button2); // Replace 'button2' with the actual ID of your button in the layout
        button3 = findViewById(R.id.button3); // Replace 'button3' with the actual ID of your button in the layout

        // Set click listeners for the buttons
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, DashBoard.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Driver_dash.class);
                startActivity(intent);
            }
        });
    }
}
