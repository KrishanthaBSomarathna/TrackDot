package com.example.slpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PassengerTicketBook extends AppCompatActivity {
    TextView a,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);

        Intent intent = getIntent();

        String busnumber = intent.getStringExtra("vehicalenumber");
        String route = intent.getStringExtra("route");

        a = findViewById(R.id.busnumber);
        b = findViewById(R.id.route);


        a.setText(busnumber);
        b.setText(route);
    }
}