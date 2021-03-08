package com.example.glacierapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fullnameSignup, passwordSignup, emailSignup, phoneSignup;
    Button signupbutton;
    TextView Textviewlogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullnameSignup = findViewById(R.id.fullnameSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        emailSignup = findViewById(R.id.emailSignup);
        phoneSignup = findViewById(R.id.phoneSignup);
        signupbutton = findViewById(R.id.signupbutton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.GONE);

        signupbutton.setOnClickListener(v -> {
            String password =passwordSignup.getText().toString().trim();
            String email =emailSignup.getText().toString().trim();
            String fullname =fullnameSignup.getText().toString().trim();
            String phone =phoneSignup.getText().toString().trim();

            if(TextUtils.isEmpty(password)){
                passwordSignup.setError("Password is required!");
                return;
            }
            if(TextUtils.isEmpty(email)){
                emailSignup.setError("email is required!");
                return;
            }
            if(password.length() <8){
                passwordSignup.setError("Password must be at least 8 characters!");
                return;
            }
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Signup.this, "User Created", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("fullname", fullname);
                        user.put("email", email);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Profile created"+ userID);
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }else {
                        Toast.makeText(Signup.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            });
        });


        Textviewlogin = findViewById(R.id.Textviewlogin);
        Textviewlogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

    }

}