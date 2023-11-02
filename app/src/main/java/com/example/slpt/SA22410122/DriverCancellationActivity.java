package com.example.slpt.SA22410122;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverCancellationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_cancelation);

        Button waitButton = findViewById(R.id.waitForDriverButton);
        Button confirmCancellationButton = findViewById(R.id.confirmCancellationButton);

        // Get the current user's UID (assuming you're using Firebase Authentication)
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        confirmCancellationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a cancellation object with details (you can modify this to match your data structure)
                Cancellation cancellation = new Cancellation(
                        userId, // Passenger's UID
                        "Reason for cancellation", // Replace with the actual reason
                        System.currentTimeMillis() // Timestamp of the cancellation
                );

                // Push the cancellation details to the database under a "cancellations" node
                DatabaseReference cancellationsRef = databaseReference.child("cancellations");
                cancellationsRef.push().setValue(cancellation);

                // Navigate to the next activity
                Intent intent = new Intent(DriverCancellationActivity.this, ConfirmCancelation.class);
                startActivity(intent);
            }
        });

        // Handle other button click as needed
        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverCancellationActivity.this, DCliveTrack.class);
                startActivity(intent);
            }
        });
    }

    private class Cancellation {
        public Cancellation(String userId, String reasonForCancellation, long currentTimeMillis) {

        }
    }
}
