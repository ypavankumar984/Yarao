package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderTrackingActivity extends AppCompatActivity {

    private TextView trackAddress;
    private Button btnTrackOrder;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Initialize the views
        trackAddress = findViewById(R.id.trackAddress);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);
        webView = findViewById(R.id.webView);

        // Enable JavaScript in the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set the tracking information in the TextView
        trackAddress.setText("Your order will be delivered to the address you provided.");

        // Set the button's onClickListener
        btnTrackOrder.setOnClickListener(v -> {
            // Show the WebView and load the HTML file from assets
            webView.setVisibility(View.VISIBLE);  // Make the WebView visible
            webView.loadUrl("file:///android_asset/maps.html");  // Load the HTML file from assets folder
        });
    }
}
