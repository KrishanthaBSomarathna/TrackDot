package com.example.slpt.SA22404350;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.slpt.R;

public class PassengerProfile extends AppCompatActivity {

    CardView additionalSetting,fullname,phone;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengerprofile);

        logout = findViewById(R.id.logout);

        additionalSetting = findViewById(R.id.additionalSetting);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Confirmation_Dialog_Logout.class));

            }
        });

        additionalSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdditionalSetting.class));
            }
        });

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PassengerName.class));

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));

            }
        });
    }
}