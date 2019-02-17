package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mUserReference;
    private User user;
    private TextView profileNameBox;
    private Switch profileAvailabilityBox;
    private final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());

        profileNameBox = findViewById(R.id.profileNameBox);
        profileAvailabilityBox = findViewById(R.id.profileAvailabilityBox);

        if (mUser == null) finish();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user == null) user = new User();

                profileAvailabilityBox.setChecked(user.availability);
                profileNameBox.setText(user.displayName);

//                Log.d(TAG, String.valueOf(user.availability));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserReference.addValueEventListener(userListener);

    }

    protected void saveProfile(View view) {
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(profileNameBox.getText().toString().trim())
//                .build();
//
//        mUser.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Successfully Saved profile ", Toast.LENGTH_LONG).show();
//                            Log.d(TAG, "User Profile Updated");
//                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Failed to Save profile " + task.getException(), Toast.LENGTH_LONG).show();
//
//                            Log.e(TAG, "failed to save profile :" + task.getException());
//                            return;
//                        }
//                    }
//                });
        writeNewUser(profileNameBox.getText().toString().trim(), mUser.getEmail(), true, null, profileAvailabilityBox.isChecked());
        Toast.makeText(getApplicationContext(), "Successfully Saved profile ", Toast.LENGTH_LONG).show();
        finish();
    }

    private void writeNewUser(String name, String email, boolean onlineStatus, HashMap<String, Object> location, boolean availability) {
        User user = new User(name, email, onlineStatus, location, availability);

        mUserReference.setValue(user);
    }
}
