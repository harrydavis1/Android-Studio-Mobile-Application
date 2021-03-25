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

public class CreateAccount extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText fullnameCreateaccount, passwordCreateaccount, emailCreateaccount, phoneCreateaccount;
    Button Createaccountbutton;
    TextView Textviewlogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        fullnameCreateaccount = findViewById(R.id.fullnamecreateaccount);
        passwordCreateaccount = findViewById(R.id.passwordcreateaccount);
        emailCreateaccount = findViewById(R.id.emailcreateaccount);
        phoneCreateaccount = findViewById(R.id.phonecreateaccount);
        Createaccountbutton = findViewById(R.id.createaccountbutton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.GONE);

        Createaccountbutton.setOnClickListener(v -> {
            String password =passwordCreateaccount.getText().toString().trim();
            String email =emailCreateaccount.getText().toString().trim();
            String fullname =fullnameCreateaccount.getText().toString().trim();
            String phone =phoneCreateaccount.getText().toString().trim();

            if(TextUtils.isEmpty(password)){
                passwordCreateaccount.setError("Password is required!");
                return;
            }
            if(TextUtils.isEmpty(email)){
                emailCreateaccount.setError("email is required!");
                return;
            }
            if(password.length() <8){
                passwordCreateaccount.setError("Password must be at least 8 characters!");
                return;
            }
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CreateAccount.this, "User Created", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreateAccount.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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