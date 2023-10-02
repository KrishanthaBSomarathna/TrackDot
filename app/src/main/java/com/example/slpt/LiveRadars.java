package com.example.slpt;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LiveRadars extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private BusAdapter busAdapter;
    private ArrayList<BusDriver> list;

    String savedbus;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_radars);

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        busAdapter = new BusAdapter(this, list);
        recyclerView.setAdapter(busAdapter);


        database.child("Passenger").child(firebaseUser.getPhoneNumber()).child("savedbus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    savedbus = snapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(),savedbus,Toast.LENGTH_LONG).show();
                    database.child("Bus Drivers").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Clear the existing list to update with the new data
                            list.clear();


                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BusDriver busDriver = dataSnapshot.getValue(BusDriver.class);

                                // Check if the busDriver's road number matches the searched road number
                                if (busDriver != null && busDriver.getVehicleNum() != null && busDriver.getVehicleNum().equals(savedbus)) {
                                    list.add(busDriver);
                                }
                            }
                            busAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database query errors here
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(),"savedbus",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










    }
}
