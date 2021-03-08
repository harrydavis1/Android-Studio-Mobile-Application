package com.example.glacierapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class contact extends AppCompatActivity {

    DrawerLayout drawerLayout;
    EditText messageText;

    Button sendBut;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    String emailto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        messageText = findViewById(R.id.messageText);

        sendBut = findViewById(R.id.sendbutton);
        drawerLayout = findViewById(R.id.drawer_layout);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();
        emailto = "davis.harry1998@gmail.com";


        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] recipents = emailto.split(",");
                String subject = "Glacier contact Form";
                String message = messageText.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipents);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("message/rfc882");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            }
        });
    }

    public void ClickMenu(View view){
        MapsActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MapsActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        MapsActivity.redirectActivity(this, MapsActivity.class);
    }

    public void ClickMyAccount(View view){
        MapsActivity.redirectActivity(this, MyAccount.class);
    }

    public void ClickSupport(View view){
        MapsActivity.redirectActivity(this, Support.class);
    }


    public void ClickContact(View view){
        recreate();
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