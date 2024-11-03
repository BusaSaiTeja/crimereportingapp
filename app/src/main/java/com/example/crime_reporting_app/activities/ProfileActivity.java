package com.example.crime_reporting_app.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.example.crime_reporting_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the frame in activity_base.xml
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.content_frame), true);

        // Retrieve and display the user's email
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        TextView emailTextView = findViewById(R.id.email_text_view);
        emailTextView.setText(userEmail);

        // Extract and display the user's name from the email
        if (userEmail != null) {
            String userName = userEmail.substring(0, userEmail.indexOf('@')); // Get name from email
            TextView nameTextView = findViewById(R.id.name_text_view); // Ensure you have this TextView in your layout
            nameTextView.setText(userName); // Set the name in the TextView
        }
    }
}
