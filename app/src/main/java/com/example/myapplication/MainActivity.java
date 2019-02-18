package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void gotoProfile(View view) {
        if (mUser != null) startActivity(new Intent(this, ProfileActivity.class));
    }

    public void gotoGPS(View view) {
        startActivity(new Intent(this, GPSActivity.class));
    }

    public void gotoMap(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void gotoSignup(View view) {
        if (mUser == null) startActivity(new Intent(this, SignupActivity.class));
    }
}
