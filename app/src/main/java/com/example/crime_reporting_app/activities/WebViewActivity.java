package com.example.crime_reporting_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize WebView
        webView = new WebView(this);
        setContentView(webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Make sure WebView loads pages correctly
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Optionally hide the loading spinner once the page has loaded
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Add a JavaScript Interface to handle communication with Android
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Load the HTML page from assets
        webView.loadUrl("file:///android_asset/map.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void receiveLocationData(String locationData) {
            // Send location data back to the Android app
            Intent resultIntent = new Intent();
            resultIntent.putExtra("locationData", locationData);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the WebViewActivity after location selection
        }
    }
}
