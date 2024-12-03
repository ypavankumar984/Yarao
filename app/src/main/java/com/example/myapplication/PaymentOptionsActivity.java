package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class PaymentOptionsActivity extends AppCompatActivity {

    private Button btnGPay, btnPhonePe, btnCOD, btnNext;
    private TextView nameTextView, addressTextView, statusTextView;
    private EditText deliveryStatusEditText;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        firestore = FirebaseFirestore.getInstance();

        // Initialize the views
        btnGPay = findViewById(R.id.btnGPay);
        btnPhonePe = findViewById(R.id.btnPhonePe);
        btnCOD = findViewById(R.id.btnCOD);
        btnNext = findViewById(R.id.btnNext);
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        deliveryStatusEditText = findViewById(R.id.deliveryStatusEditText);
        statusTextView = findViewById(R.id.statusTextView);

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
            // Display waiting for delivery message
            deliveryStatusEditText.setText("Waiting for delivery...");

            // Send notification to couriers
            sendNotificationToCouriers();

            // Open the OrderTrackingActivity after sending the notification
            Intent intent = new Intent(PaymentOptionsActivity.this, OrderTrackingActivity.class);
            startActivity(intent);
        });
    }

    private void sendNotificationToCouriers() {
        // Query Firestore for users with role 'Courier'
        firestore.collection("users")
                .whereEqualTo("role", "Courier")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Extract courier FCM tokens from Firestore
                            for (DocumentSnapshot document : querySnapshot) {
                                String fcmToken = document.getString("fcmToken");
                                if (fcmToken != null) {
                                    // Send notification to each courier
                                    sendFCMNotification(fcmToken);
                                }
                            }
                        }
                    }
                });
    }

    private void sendFCMNotification(String fcmToken) {
        // Create a message for the courier
        RemoteMessage message = new RemoteMessage.Builder(fcmToken)
                .setMessageId("1")
                .addData("title", "New Delivery Request")
                .addData("body", "Please check your delivery queue")
                .build();

        // Send the notification to the courier via Firebase
        FirebaseMessaging.getInstance().send(message);
    }
}
