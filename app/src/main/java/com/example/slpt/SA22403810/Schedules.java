package com.example.slpt.SA22403810;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;


public class Schedules extends AppCompatActivity {
    

    Button btnSearch;
    EditText autoCompleteOrigin,autoCompleteDestination;
    String origin,destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        btnSearch = findViewById(R.id.btnSearch);
        autoCompleteOrigin = findViewById(R.id.autoCompleteOrigin);
        autoCompleteDestination = findViewById(R.id.autoCompleteDestination);


        AutoCompleteTextView autoCompleteOrigin = findViewById(R.id.autoCompleteOrigin);
        AutoCompleteTextView autoCompleteDestination = findViewById(R.id.autoCompleteDestination);

        // Get the array of suggestions from strings.xml
        String[] suggestions = getResources().getStringArray(R.array.autocomplete_suggestions);

        // Create an ArrayAdapter for the AutoCompleteTextViews
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);

        // Set the adapter for the AutoCompleteTextViews
        autoCompleteOrigin.setAdapter(adapter);
        autoCompleteDestination.setAdapter(adapter);


            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    origin = autoCompleteOrigin.getText().toString();
                    destination = autoCompleteDestination.getText().toString();
                    Intent intent = new Intent(Schedules.this, RouteFind.class);
                    intent.putExtra("origin",origin);
                    intent.putExtra("destination",destination);
                    intent.putExtra("route",origin+" ➤ "+destination).toString();
                    startActivity(intent);
                }
            });


    }


}