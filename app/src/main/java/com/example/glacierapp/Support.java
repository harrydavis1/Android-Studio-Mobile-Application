package com.example.glacierapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Support extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, fullname, phone;
    FirebaseUser user;
    TextView moreInfobut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        drawerLayout = findViewById(R.id.drawer_layout);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();
        moreInfobut = findViewById(R.id.moreInfobut);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                fullname = documentSnapshot.getString("fullname");
                phone = documentSnapshot.getString("phone");

            }
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        moreInfobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        Support.this);
                LayoutInflater inflater = Support.this.getLayoutInflater();
                View mView = inflater.inflate(R.layout.supportinfodesc, null);
                final EditText descText = (EditText)mView.findViewById(R.id.descText);
                descText.setText("Mind Charity Tel: 011111111 \nEmail: mind@email.com \nAddress: Mind Charity, Mind Lane, London, E32 7HB");

                builder.setView(mView).setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialContactPhone("+447747851396");


                    }
                })
                        .setNegativeButton("Text Message", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendingSms();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void sendingSms(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+447747851396", null, "Hello this is " + fullname + " Can you contact me on " + phone + " This is my location: https://www.google.com/maps/dir/?api=1&destination=lat,lng", null, null);
        Toast.makeText(getApplicationContext(), "TEXT MESSAGE SENT",
                Toast.LENGTH_LONG).show();
    }

    public void ClickMenu(View view){
        MapsActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MapsActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){MapsActivity.redirectActivity(this, MapsActivity.class);
    }

    public void ClickContact(View view){
        MapsActivity.redirectActivity(this, contact.class);
    }

    public void ClickSupport(View view){
        recreate();
    }

    public void ClickMyAccount(View view){
        MapsActivity.redirectActivity(this, MyAccount.class);
    }

    public void ClickLogOut(View view){
        MapsActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MapsActivity.closeDrawer(drawerLayout);
    }
}