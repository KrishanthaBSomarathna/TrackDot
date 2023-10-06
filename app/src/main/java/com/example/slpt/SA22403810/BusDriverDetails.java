package com.example.slpt.SA22403810;

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
import com.example.slpt.SA22404350.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BusDriverDetails extends AppCompatActivity {

    private EditText vehicleNumber,vehicleName,roadNumber,seatCount,startDestination,stopDestination,r1StartTime, r1StopTime, r2StartTime, r2StopTime;
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
        setContentView(R.layout.activity_bus_driver_details);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            startActivity(new Intent(this, Register.class));
        }

        vehicleNumber = findViewById(R.id.vehicleNumber);
        vehicleName = findViewById(R.id.vehiclename);
        roadNumber = findViewById(R.id.roadNumber);
        seatCount = findViewById(R.id.seatCount);
        startDestination = findViewById(R.id.startdestination);
        stopDestination = findViewById(R.id.stopDestination);


        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);

        emptyfields = findViewById(R.id.emptyfields);
        emptyfields.setVisibility(View.GONE);


        savedata = findViewById(R.id.save);


        r1StartTime = findViewById(R.id.r1starttime);
        r1StartTime = findViewById(R.id.r1starttime);
        r1StartTime = findViewById(R.id.r1starttime);
        r1StartTime = findViewById(R.id.r1starttime);






        r1StartTime = findViewById(R.id.r1starttime);
        r1StopTime = findViewById(R.id.r1stopttime);
        r2StartTime = findViewById(R.id.r2starttime);
        r2StopTime = findViewById(R.id.r2stopttime);

        setupTimePicker(r1StartTime);
        setupTimePicker(r1StopTime);
        setupTimePicker(r2StartTime);
        setupTimePicker(r2StopTime);


        savedata.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View view) {
                String vnum = vehicleNumber.getText().toString();
                String vname = vehicleName.getText().toString();
                String seatcount = seatCount.getText().toString();
                String rnum = roadNumber.getText().toString();
                String bustype = vehicleTypeSpinner.getSelectedItem().toString().trim();
                String startdes = startDestination.getText().toString();
                String stopdes = stopDestination.getText().toString();
                String r1start = r1StartTime.getText().toString();
                String r1stop = r1StopTime.getText().toString();
                String r2start = r2StartTime.getText().toString();
                String r2stop = r2StopTime.getText().toString();

                if(!bustype.equals("Select Bus Type")&&!vnum.equals("")&&!seatcount.equals("")&&!rnum.equals("")&&!startdes.equals("")&&!stopdes.equals("")&&!r1start.equals("")&&!r1stop.equals("")&&!r2start.equals("")&&!r2stop.equals(""))
                {


                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("vehicleNum").setValue(vnum);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("vehicleName").setValue(vname);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("seatcount").setValue(seatcount);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("roadnumber").setValue(rnum);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("startdestination").setValue(startdes);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("stopdestination").setValue(stopdes);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("r1start").setValue(r1start);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("r1stop").setValue(r1stop);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("r2start").setValue(r2start);
                        databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("r2stop").setValue(r2stop);
                    databaseReference.child("Bus Drivers").child(firebaseUser.getPhoneNumber()).child("bustype").setValue(bustype);

                        startActivity(new Intent(BusDriverDetails.this, BusDriverView.class));

                }
                else
                {
                    emptyfields.setVisibility(View.VISIBLE);
                    vehicleNumber.setBackgroundResource(R.drawable.editbg_red_border);
                    roadNumber.setBackgroundResource(R.drawable.editbg_red_border);
                    seatCount.setBackgroundResource(R.drawable.editbg_red_border);
                    stopDestination.setBackgroundResource(R.drawable.editbg_red_border);
                    startDestination.setBackgroundResource(R.drawable.editbg_red_border);
                    r1StartTime.setBackgroundResource(R.drawable.editbg_red_border);
                    r1StopTime.setBackgroundResource(R.drawable.editbg_red_border);
                    r2StartTime.setBackgroundResource(R.drawable.editbg_red_border);
                    r2StopTime.setBackgroundResource(R.drawable.editbg_red_border);
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