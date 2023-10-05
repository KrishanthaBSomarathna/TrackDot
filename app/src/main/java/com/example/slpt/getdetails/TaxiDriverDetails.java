package com.example.slpt.getdetails;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slpt.R;
import com.example.slpt.TaxiDriver;
import com.example.slpt.register.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaxiDriverDetails extends AppCompatActivity {

    private EditText vehicleNumber,nic,seatCount;
    private Spinner vehicleTypeSpinner;
    private Button savedata;
    private TextView emptyfields;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_driver_details);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            startActivity(new Intent(this, Register.class));
        }

        vehicleNumber = findViewById(R.id.vehicleNumber);
        nic = findViewById(R.id.nic);
        seatCount = findViewById(R.id.seatCount);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
        emptyfields = findViewById(R.id.emptyfields);
        emptyfields.setVisibility(View.GONE);
        savedata = findViewById(R.id.save);
        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vnum = vehicleNumber.getText().toString();
                String seatcount = seatCount.getText().toString();
                String taxitype = vehicleTypeSpinner.getSelectedItem().toString().trim();
                String nicc = nic.getText().toString();

                if(!taxitype.equals("Select Taxi Type")&&!vnum.equals("")&&!seatcount.equals("")&&!nicc.equals(""))
                {

                    databaseReference.child("Taxi Driver").child(firebaseUser.getPhoneNumber()).child("vehicleNum").setValue(vnum);
                    databaseReference.child("Taxi Driver").child(firebaseUser.getPhoneNumber()).child("seatcount").setValue(seatcount);
                    databaseReference.child("Taxi Driver").child(firebaseUser.getPhoneNumber()).child("taxitype").setValue(taxitype);
                    databaseReference.child("Taxi Driver").child(firebaseUser.getPhoneNumber()).child("nic").setValue(nicc);

                    startActivity(new Intent(TaxiDriverDetails.this, TaxiDriver.class));

                }
                else
                {
                    emptyfields.setVisibility(View.VISIBLE);
                    vehicleNumber.setBackgroundResource(R.drawable.editbg_red_border);
                    seatCount.setBackgroundResource(R.drawable.editbg_red_border);
                    nic.setBackgroundResource(R.drawable.editbg_red_border);

                    vehicleTypeSpinner.setBackgroundResource(R.drawable.editbg_red_border);
                }
            }
        });



    }

    private void setupTimePicker(final EditText editText) {
        editText.setFocusable(false);
        editText.setClickable(true);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(editText);
            }
        });
    }

    private void showTimePickerDialog(final EditText editText) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String formattedTime = formatTime(hourOfDay, minute);
                        editText.setText(formattedTime);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
    }
}