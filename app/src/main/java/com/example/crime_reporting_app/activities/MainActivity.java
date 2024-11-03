package com.example.crime_reporting_app.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.example.crime_reporting_app.R;
import com.example.crime_reporting_app.adapters.CrimeReportAdapter;
import com.example.crime_reporting_app.models.CrimeReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CrimeReportAdapter adapter;
    private List<CrimeReport> crimeReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.content_frame), true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        crimeReports = new ArrayList<>();
        adapter = new CrimeReportAdapter(crimeReports, this);
        recyclerView.setAdapter(adapter);

        loadCrimeReports();
    }
    private void loadCrimeReports() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("crimeReports")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(100)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        // Handle error, e.g., log it
                        return;
                    }

                    if (value != null) {
                        crimeReports.clear(); // Clear the previous list
                        for (QueryDocumentSnapshot doc : value) {
                            // Retrieve data from the document
                            String username = doc.getString("username");
                            String type = doc.getString("type");
                            String location = doc.getString("location");
                            String description = doc.getString("description");
                            String mediaUrl = doc.getString("mediaUrl");

                            // Log each report being added
                            Log.d("CrimeReport", "Adding report: " + username + ", " + type + ", " + description);

                            // Add to the list
                            crimeReports.add(new CrimeReport(username, type,location, description, mediaUrl));
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter of data change
                        Log.d("CrimeReport", "Total reports loaded: " + crimeReports.size());
                    }
                });
    }


}
