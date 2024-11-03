package com.example.crime_reporting_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.crime_reporting_app.R;

public class CrimeVisualizationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_crime_visualization, findViewById(R.id.content_frame), true);
    }
}
