package com.example.glacierapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity {

    DrawerLayout drawerLayout;
    EditText fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    Button doneButton, resetbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        drawerLayout = findViewById(R.id.drawer_layout);
        fullName = findViewById(R.id.MyAccountFullName);
        email = findViewById(R.id.MyAccountEmail);
        phone = findViewById(R.id.MyAccountPhone);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();

        resetbutton = findViewById(R.id.MyAccountResetButton);
        doneButton = findViewById(R.id.MyAccountDoneButton);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                fullName.setText(documentSnapshot.getString("fullname"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPass = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter new password");
                passwordResetDialog.setView(resetPass);
                passwordResetDialog.setPositiveButton("Done", (dialog, which) -> {
                    String newPassword = resetPass.getText().toString();
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MyAccount.this, "Password Reset", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyAccount.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {
                });
                passwordResetDialog.create().show();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()){
                    Toast.makeText(MyAccount.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String profEmail = email.getText().toString();
                user.updateEmail(profEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", profEmail);
                        edited.put("fullname", fullName.getText().toString());
                        edited.put("phone", phone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MyAccount.this, "Account Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });



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
        MapsActivity.redirectActivity(this, Support.class);
    }

    public void ClickMyAccount(View view){
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