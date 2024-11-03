package com.example.crime_reporting_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.example.crime_reporting_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import android.widget.ArrayAdapter;

public class UploadCrimeActivity extends BaseActivity {

    private Spinner spinnerCrimeType;
    private EditText editTextDescription, editTextLocation;
    private Button buttonSelectMedia, buttonSubmitCrime;
    private Uri mediaUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_upload_crime, findViewById(R.id.content_frame), true); // Inflate layout

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("crime_media"); // Specify the storage path

        // Initialize UI components
        spinnerCrimeType = findViewById(R.id.spinner_crime_type);
        editTextDescription = findViewById(R.id.edittext_description);
        editTextLocation = findViewById(R.id.edittext_location);
        buttonSelectMedia = findViewById(R.id.button_select_media);
        buttonSubmitCrime = findViewById(R.id.button_submit_crime);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crime_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrimeType.setAdapter(adapter);

        // Set click listeners
        buttonSelectMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMedia();
            }
        });

        buttonSubmitCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCrimeReport();
            }
        });
    }

    private void selectMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Change to "image/*" if you only want images
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            mediaUri = data.getData(); // Get the URI of the selected media
        }
    }

    private void submitCrimeReport() {
        String crimeType = spinnerCrimeType.getSelectedItem().toString();
        String username = getUsernameFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()) ;
        String description = editTextDescription.getText().toString();
        String location = editTextLocation.getText().toString();

        if (description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return; // Exit if any field is empty
        }

        if (mediaUri != null) {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

            // Upload media if URI is available
            fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get download URL and save report
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String mediaUrl = uri.toString();
                            saveCrimeReport(crimeType,username, description, location, mediaUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadCrimeActivity.this, "Error getting media URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadCrimeActivity.this, "Error uploading media: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If no media is selected, proceed to save the report with null media URL
            saveCrimeReport(crimeType,username, description, location, null);
        }
    }
    private String getUsernameFromEmail(String email) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf("@")); // Extract username from email
        }
        return "Unknown"; // Default username if email is not valid
    }


    private void saveCrimeReport(String type,String username, String description, String location, String mediaUrl) {
        Map<String, Object> crimeReport = new HashMap<>();
        crimeReport.put("type", type);
        crimeReport.put("username", username);
        crimeReport.put("description", description);
        crimeReport.put("location", location);
        crimeReport.put("timestamp", System.currentTimeMillis());
        crimeReport.put("mediaUrl", mediaUrl);

        db.collection("crimeReports").add(crimeReport).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadCrimeActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(UploadCrimeActivity.this, "Error submitting report", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
