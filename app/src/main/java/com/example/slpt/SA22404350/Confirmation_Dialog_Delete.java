package com.example.slpt.SA22404350;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.slpt.R;
import com.google.firebase.auth.FirebaseAuth;

public class Confirmation_Dialog_Delete extends FragmentActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_setting); // Replace with your layout XML

        firebaseAuth = FirebaseAuth.getInstance();
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title and message
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to Delete Account");

        // Set the positive button (confirm button) and its click listener
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.getCurrentUser().delete();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Register.class));
                Animatoo.INSTANCE.animateFade(Confirmation_Dialog_Delete.this);
            }
        });

        // Set the negative button (optional) and its click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Define the action when the user cancels (optional)
                finish();
                Animatoo.INSTANCE.animateFade(Confirmation_Dialog_Delete.this);
            }
        });

        // Set the AlertDialog as cancelable
        builder.setCancelable(true);

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Add a cancel listener to execute finish() when the dialog is canceled
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                Animatoo.INSTANCE.animateFade(Confirmation_Dialog_Delete.this);
            }
        });

        // Allow clicking anywhere else on the screen to dismiss the dialog
        dialog.setCanceledOnTouchOutside(true);
    }
}
