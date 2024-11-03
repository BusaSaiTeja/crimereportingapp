package com.example.crime_reporting_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crime_reporting_app.R;
import com.example.crime_reporting_app.models.CrimeReport;

import java.util.List;

public class CrimeReportAdapter extends RecyclerView.Adapter<CrimeReportAdapter.CrimeReportViewHolder> {

    private List<CrimeReport> crimeReports;
    private Context context;

    public CrimeReportAdapter(List<CrimeReport> crimeReports, Context context) {
        this.crimeReports = crimeReports;
        this.context = context;
    }

    @NonNull
    @Override
    public CrimeReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_crime_report, parent, false);
        return new CrimeReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeReportViewHolder holder, int position) {
        CrimeReport crimeReport = crimeReports.get(position);
        holder.usernameTextView.setText(crimeReport.getUsername());
        holder.typeTextView.setText("Type: " + crimeReport.getType());
        holder.locationTextView.setText("Location: " + crimeReport.getLocation());
        holder.descriptionTextView.setText("Description: " + crimeReport.getDescription());

        if (crimeReport.getMediaUrl() != null && !crimeReport.getMediaUrl().isEmpty()) {
            holder.mediaImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(crimeReport.getMediaUrl()).into(holder.mediaImageView);

            holder.itemView.setPadding(0, 16, 0, 16);
        } else {
            holder.mediaImageView.setVisibility(View.GONE);

            holder.itemView.setPadding(0, 16, 0, 8);
        }
    }


    @Override
    public int getItemCount() {
        return crimeReports.size(); // Return the total number of items
    }

    public static class CrimeReportViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView typeTextView;
        public TextView locationTextView; // Declare locationTextView
        public TextView descriptionTextView;
        public ImageView mediaImageView;

        public CrimeReportViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            typeTextView = itemView.findViewById(R.id.type_text_view);
            locationTextView = itemView.findViewById(R.id.location_text_view); // Initialize locationTextView
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            mediaImageView = itemView.findViewById(R.id.media_image_view); // If you are displaying media
        }
    }
}
