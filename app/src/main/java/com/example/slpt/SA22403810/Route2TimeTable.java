package com.example.slpt.SA22403810;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Route2TimeTable extends AppCompatActivity {

    private LinearLayout parentLayout;
    private Button saveButton;
    private ArrayList<String> locationNames = new ArrayList<>(); // Store location names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route2_time_table);

        parentLayout = findViewById(R.id.parentLayout); // Assuming you have this layout in your XML
        saveButton = findViewById(R.id.saveButton); // Get a reference to the "Save to Firebase" button

        // Set an OnClickListener for the button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFirebase();
                startActivity(new Intent(getApplicationContext(), BusDriverView.class));
            }
        });

        Intent intent = getIntent();

        String routeCode = intent.getStringExtra("route");

        // Reference to the "Route" node in your Firebase database
        DatabaseReference routeReference = FirebaseDatabase.getInstance().getReference().child("Route").child(routeCode);

        routeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationNames.clear(); // Clear the existing data

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue(String.class);
                    locationNames.add(location);
                }

                createEditTextFields(); // After fetching data, create EditText fields
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur
            }
        });
    }

//    private void createEditTextFields() {
//        for (int i = 1; i < locationNames.size(); i++) {
//            String location = locationNames.get(i);
//            EditText editText = new EditText(this);
//            editText.setHint("Enter time for " + location);
//            parentLayout.addView(editText);
//        }
//    }

    private void createEditTextFields() {
        for (int i = locationNames.size() - 1; i >= 1; i--) {
            String location = locationNames.get(i);
            EditText editText = new EditText(this);

            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            // Set background
            editText.setBackgroundResource(R.drawable.editbg_black_border); // You can create a custom drawable for the background

            // Set width to match parent
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText.setLayoutParams(params);

            // Set height to 100dp
            params.height = (int) getResources().getDimension(R.dimen.edittext_height);

            // Set padding
            editText.setPadding(
                    (int) getResources().getDimension(R.dimen.edittext_padding), // Left padding
                    (int) getResources().getDimension(R.dimen.edittext_padding), // Top padding
                    (int) getResources().getDimension(R.dimen.edittext_padding), // Right padding
                    (int) getResources().getDimension(R.dimen.edittext_padding)  // Bottom padding
            );

            // Set margins
            params.setMargins(
                    (int) getResources().getDimension(R.dimen.edittext_margin), // Left margin
                    (int) getResources().getDimension(R.dimen.edittext_margin), // Top margin
                    (int) getResources().getDimension(R.dimen.edittext_margin), // Right margin
                    (int) getResources().getDimension(R.dimen.edittext_margin)  // Bottom margin
            );

            // Set hint
            editText.setHint("Enter time for " + location);

            // Add the EditText to the parent layout
            parentLayout.addView(editText);
        }
    }







    private void saveToFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bus Drivers"); // Modify this path according to your data structure

        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            if (childView instanceof EditText) {
                EditText editText = (EditText) childView;
                String originalLocation = editText.getHint().toString().replace("Enter time for ", "");
                String sanitizedLocation = originalLocation.replaceAll("[^a-zA-Z0-9]", ""); // Remove special characters
                String time = editText.getText().toString();

                // Save the time to Firebase
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString()).child("r2timetable").child(sanitizedLocation).setValue(time);
            }
        }
    }
}
