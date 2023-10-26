package com.example.slpt.SA22404350;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.slpt.R;

public class AdditionalSetting extends AppCompatActivity {

    CardView deleteAcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_setting);

        deleteAcount = findViewById(R.id.deleteAccount);

        deleteAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Confirmation_Dialog_Delete.class));
            }
        });
    }
}