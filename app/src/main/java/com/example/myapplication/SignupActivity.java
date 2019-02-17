package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private final String TAG = "SignUpActivity";
    private EditText inputEmail, inputPassword;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Not Signed in!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "User: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }


        btnSignUp = findViewById(R.id.signupBtn);
        inputEmail = findViewById(R.id.emailBox);
        inputPassword = findViewById(R.id.passwordBox);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, minimum size is 6!", Toast.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "Auth Failed: " + task.getException());
                                } else {
                                    Toast.makeText(SignupActivity.this, "Signup successful! " + task.isSuccessful(), Toast.LENGTH_LONG).show();
//                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
