package com.example.slpt.SA22404350;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.example.slpt.SA22403810.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NameChange extends AppCompatActivity {
DatabaseReference databaseReference;

    private String mobile,type;
    private Button update;
    EditText updatedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        updatedName = findViewById(R.id.usernameupdate);
        mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        update = findViewById(R.id.save);

        Intent intent = getIntent();

         type = intent.getStringExtra("Type");


        

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namestr = (String) snapshot.child(type).child(mobile).child("UserName").getValue();
                updatedName.setText(namestr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNameText = updatedName.getText().toString();
              databaseReference.child(type).child(mobile).child("UserName").setValue(updatedNameText);
                Toast.makeText(getApplicationContext(),"Name Updated Succussfull",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Animatoo.INSTANCE.animateFade(NameChange.this);
                    }
                }, 1500);
            }
        });

    }
}