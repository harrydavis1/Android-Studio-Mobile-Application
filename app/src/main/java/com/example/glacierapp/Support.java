package com.example.glacierapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
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
    Button SmsButton, CallButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, fullname, phone;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        drawerLayout = findViewById(R.id.drawer_layout);
        SmsButton = findViewById(R.id.smsbutton);
        CallButton = findViewById(R.id.callbutton);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                fullname = documentSnapshot.getString("fullname");
                phone = documentSnapshot.getString("phone");

            }
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        SmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingSms();
            }
        });
        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("+447747851396");
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