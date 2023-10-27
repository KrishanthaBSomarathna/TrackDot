package com.example.slpt.SA22410122;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;

public class Entry2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry2);

        Button buttonshift1=findViewById(R.id.shifting);

        buttonshift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          startActivity(new Intent(Entry2.this, SetLocation.class));
            }
        });
    }

}