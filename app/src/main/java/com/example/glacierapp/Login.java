package com.example.glacierapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    Button loginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBarLogin;
    TextView Textviewsignup, forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        fAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        Textviewsignup = findViewById(R.id.Textviewsignup);
        forgotpassword = findViewById(R.id.forgotpassword);


        loginButton.setOnClickListener(v -> {
            String password = passwordLogin.getText().toString().trim();
            String email = emailLogin.getText().toString().trim();

            if (TextUtils.isEmpty(password)) {
                passwordLogin.setError("Password is required!");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailLogin.setError("email is required!");
                return;
            }
            if (password.length() < 8) {
                passwordLogin.setError("Password must be at least 8 characters!");
                return;
            }
            progressBarLogin.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, R.string.Toast_Login_Successful, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                } else {
                    Toast.makeText(Login.this, "Incorrect Email/Password " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);

                }
            });
        });


        Textviewsignup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
            startActivity(intent);
            finish();
        });

        forgotpassword.setOnClickListener(v -> {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter email for the reset link to be sent.");
            passwordResetDialog.setView(resetMail);
            passwordResetDialog.setPositiveButton("Send", (dialog, which) -> {
                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this, "Reset Link has been sent to your email.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {

            });
            passwordResetDialog.create().show();
        });

    }
}