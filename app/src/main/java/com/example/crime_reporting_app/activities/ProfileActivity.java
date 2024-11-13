package com.example.crime_reporting_app.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.example.crime_reporting_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.content_frame), true);

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        TextView emailTextView = findViewById(R.id.email_text_view);
        emailTextView.setText(userEmail);

        if (userEmail != null) {
            String userName = userEmail.substring(0, userEmail.indexOf('@')); // Get name from email
            TextView nameTextView = findViewById(R.id.name_text_view); // Ensure you have this TextView in your layout
            nameTextView.setText(userName); // Set the name in the TextView
        }
    }
}