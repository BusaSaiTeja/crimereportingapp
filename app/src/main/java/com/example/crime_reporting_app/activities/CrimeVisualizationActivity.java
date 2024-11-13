package com.example.crime_reporting_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.crime_reporting_app.R;
import com.example.crime_reporting_app.models.CrimeReport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CrimeVisualizationActivity extends AppCompatActivity {

    private WebView webView;
    private CollectionReference crimeDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_visualization);

        WebView.setWebContentsDebuggingEnabled(true); // Enable debugging for WebView

        // Initialize the WebView
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Pass the crime data after the page is fully loaded
                passCrimeDataToWebView();
            }
        });

        // Load the map HTML page
        webView.loadUrl("file:///android_asset/mapvisualization.html");

        // Initialize Firestore reference to the "crimeReports" collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        crimeDataRef = db.collection("crimeReports");
    }

    private void passCrimeDataToWebView() {
        // Retrieve crime reports from Firestore
        crimeDataRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CrimeReport> crimeReports = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String description = document.getString("description");
                    String locationJson = document.getString("location");
                    String type = document.getString("type");
                    String username = document.getString("username");
                    String mediaUrl = document.getString("mediaUrl");

                    // Parse the location (lat, lng)
                    try {
                        JSONObject locationObj = new JSONObject(locationJson);
                        double lat = locationObj.getDouble("lat");
                        double lng = locationObj.getDouble("lng");

                        // Create CrimeReport object and add it to the list
                        CrimeReport report = new CrimeReport(username, type, locationJson, description, mediaUrl);
                        crimeReports.add(report);

                    } catch (Exception e) {
                        Log.e("CrimeVisualization", "Error parsing location", e);
                    }
                }

                sendCrimeDataToWebView(crimeReports);
            } else {
                Log.e("CrimeVisualization", "Firestore error: " + task.getException());
            }
        });
    }

    // Send the crime data to the WebView
    private void sendCrimeDataToWebView(List<CrimeReport> crimeReports) {
        try {
            JSONArray crimeReportsJsonArray = new JSONArray();

            for (CrimeReport report : crimeReports) {
                JSONObject reportJson = new JSONObject();
                reportJson.put("description", report.getDescription());
                reportJson.put("type", report.getType());

                // Extract location from the location string and get lat/lng
                JSONObject locationJson = extractLatLngFromLocationString(report.getLocation());
                reportJson.put("lat", locationJson.getDouble("lat"));
                reportJson.put("lng", locationJson.getDouble("lng"));

                crimeReportsJsonArray.put(reportJson);
            }

            // Log the crime data to check if it is being passed correctly
            Log.d("CrimeData", "Crime reports JSON array: " + crimeReportsJsonArray.toString());

            // Pass the crime data to the WebView via JavaScript
            String crimeDataJson = crimeReportsJsonArray.toString();
            webView.evaluateJavascript("javascript:addCrimeData(" + crimeDataJson + ");", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract lat/lng from the location JSON string
    private JSONObject extractLatLngFromLocationString(String locationJsonString) {
        JSONObject locationJson = new JSONObject();
        try {
            JSONObject locationObj = new JSONObject(locationJsonString);
            double lat = locationObj.getDouble("lat");
            double lng = locationObj.getDouble("lng");

            locationJson.put("lat", lat);
            locationJson.put("lng", lng);
        } catch (Exception e) {
            Log.e("CrimeVisualization", "Error extracting lat/lng", e);
        }
        return locationJson;
    }
}
