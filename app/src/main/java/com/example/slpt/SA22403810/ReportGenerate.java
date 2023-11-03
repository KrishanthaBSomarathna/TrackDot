package com.example.slpt.SA22403810;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.slpt.R;

public class ReportGenerate extends AppCompatActivity {

    CardView bus,passenger,ticket,taxi,cargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generate);
        bus = findViewById(R.id.bus);
        passenger = findViewById(R.id.passenger);
        ticket = findViewById(R.id.ticket);
        taxi = findViewById(R.id.taxi);
        cargo = findViewById(R.id.cargo);

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://rzv73zqwuss3l69tszkjzw.on.drv.tw/www.buslocation.com/";

                // Create an intent with the ACTION_VIEW action and the URL
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Start the intent to open the URL in an external browser
                startActivity(intent);
            }
        });
        passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mxucr5oe969xdg4zdfl7pg.on.drv.tw/www.passengerreport.com/";

                // Create an intent with the ACTION_VIEW action and the URL
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Start the intent to open the URL in an external browser
                startActivity(intent);
            }
        });
    }
}