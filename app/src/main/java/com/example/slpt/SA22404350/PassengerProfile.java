package com.example.slpt.SA22404350;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.example.slpt.SA22403810.BusDriverView;
import com.example.slpt.SA22403810.PassengerMainView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PassengerProfile extends AppCompatActivity {

    CardView additionalSetting,fullname,phone;
    TextView logout,name,phoneNumber;

    String mobile;
    DatabaseReference databaseReference;
    LinearLayout home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        home = findViewById(R.id.home);
        mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);

        additionalSetting = findViewById(R.id.additionalSetting);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PassengerMainView.class));

                Animatoo.INSTANCE.animateSlideRight(PassengerProfile.this);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Confirmation_Dialog_Logout.class));
                Animatoo.INSTANCE.animateFade(PassengerProfile.this);
            }
        });

        additionalSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdditionalSetting.class);
                intent.putExtra("type","Passenger");
                startActivity(intent);

            }
        });

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NameChange.class);
                intent.putExtra("Type","Passenger");
                startActivity(intent);


            }
        });




        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String namestr = (String) snapshot.child("Passenger").child(mobile).child("UserName").getValue();
               name.setText(namestr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        phoneNumber.setText(mobile.replace("+94","0"));
    }
    public void onBackPressed() {
        // Handle back button press, navigate to home screen
        finish();
    }
}