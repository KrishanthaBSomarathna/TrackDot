package com.example.slpt.SA22403810;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.LoginHandalor;
import com.example.slpt.R;
import com.example.slpt.SA22404350.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!NetworkUtil.isNetworkAvailable(this)) {
            // No internet connection, show a dialog to inform the user
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect to the internet and try again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Check for internet again or take appropriate action
                            if (NetworkUtil.isNetworkAvailable(SplashScreen.this)) {
                                // Internet is available, continue the app
                                handleUserType();
                            } else {
                                recreate();
                            }
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Exit the app or take appropriate action
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            // Internet is available, proceed with handling user type
            handleUserType();
        }
    }

    private void handleUserType() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    Animatoo.INSTANCE.animateFade(SplashScreen.this);
                }
            }, 1500);
        } else {
            mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            DatabaseReference driversReference = databaseReference.child("Bus Drivers").child(mobile);
            DatabaseReference passengersReference = databaseReference.child("Passenger").child(mobile);
            DatabaseReference taxiDriversReference = databaseReference.child("Taxi Driver").child(mobile);
            DatabaseReference cargoDriversReference = databaseReference.child("Cargo Driver").child("mobile");

            driversReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // The mobile number corresponds to a Bus Driver.
                        // Display this information to the user.
                        Toast.makeText(getApplicationContext(), "Bus Driver", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), BusDriverView.class));
                                Animatoo.INSTANCE.animateFade(SplashScreen.this);
                            }
                        }, 1500);
                    } else {
                        // If not a Bus Driver, check for Passenger.
                        passengersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // The mobile number corresponds to a Passenger.
                                    // Display this information to the user.
                                    Toast.makeText(getApplicationContext(), "Passenger", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getApplicationContext(), PassengerMainView.class));
                                            Animatoo.INSTANCE.animateFade(SplashScreen.this);
                                        }
                                    }, 1500);
                                } else {
                                    // If not a Passenger, check for Taxi Driver.
                                    taxiDriversReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // The mobile number corresponds to a Taxi Driver.
                                                // Display this information to the user.
                                                Toast.makeText(getApplicationContext(), "Taxi", Toast.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Animatoo.INSTANCE.animateFade(SplashScreen.this);
                                                    }
                                                }, 1500);
                                            } else {
                                                // The mobile number doesn't match any of the categories.
                                                // Display a message to the user indicating that.
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle database errors if necessary.
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle database errors if necessary.
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database errors if necessary.
                }
            });
        }
    }
}
