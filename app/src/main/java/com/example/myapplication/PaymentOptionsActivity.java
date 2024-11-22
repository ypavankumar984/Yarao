package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentOptionsActivity extends AppCompatActivity {

    private Button btnGPay, btnPhonePe, btnCOD, btnNext;
    private TextView nameTextView, addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        // Initialize the views
        btnGPay = findViewById(R.id.btnGPay);
        btnPhonePe = findViewById(R.id.btnPhonePe);
        btnCOD = findViewById(R.id.btnCOD);
        btnNext = findViewById(R.id.btnNext);
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);

        // Retrieve the name and address from the Intent
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");

        // Display the received details
        if (name != null && address != null) {
            nameTextView.setText("Name: " + name);
            addressTextView.setText("Address: " + address);
        } else {
            nameTextView.setText("Name: Unknown");
            addressTextView.setText("Address: Unknown");
        }

        // Handle payment option clicks
        btnGPay.setOnClickListener(v -> {
            btnNext.setVisibility(View.VISIBLE);  // Show Next button after selecting payment
        });

        btnPhonePe.setOnClickListener(v -> {
            btnNext.setVisibility(View.VISIBLE);  // Show Next button after selecting payment
        });

        btnCOD.setOnClickListener(v -> {
            btnNext.setVisibility(View.VISIBLE);  // Show Next button after selecting payment
        });

        // Handle Next button click to navigate to OrderTrackingActivity
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentOptionsActivity.this, OrderTrackingActivity.class);
            // If any additional data is required, pass it to OrderTrackingActivity
            startActivity(intent);
        });
    }
}
