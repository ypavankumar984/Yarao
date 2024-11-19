package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView orderMessageTextView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private Button btnConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize the views
        orderMessageTextView = findViewById(R.id.orderMessage);
        productNameTextView = findViewById(R.id.productName);
        productPriceTextView = findViewById(R.id.productPrice);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Get the product details from the Intent
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");

        // Display the order confirmation message and product details
        orderMessageTextView.setText("Order Confirmed!");
        productNameTextView.setText("Product: " + productName);
        productPriceTextView.setText("Price: " + productPrice);

        // Set up the payment options
        setUpPaymentMethods();
    }

    private void setUpPaymentMethods() {
        Button btnGPay = findViewById(R.id.btnGPay);
        Button btnPhonePe = findViewById(R.id.btnPhonePe);
        Button btnCOD = findViewById(R.id.btnCOD);
        Button btnEMI = findViewById(R.id.btnEMI);
        Button btnCreditCard = findViewById(R.id.btnCreditCard);
        Button btnDebitCard = findViewById(R.id.btnDebitCard);
        Button btnNetBanking = findViewById(R.id.btnNetBanking);
        Button btnUPI = findViewById(R.id.btnUPI);

        // Set OnClickListener for each payment option
        btnGPay.setOnClickListener(v -> showPaymentConfirmation("GPay"));
        btnPhonePe.setOnClickListener(v -> showPaymentConfirmation("PhonePe"));
        btnCOD.setOnClickListener(v -> showPaymentConfirmation("Cash on Delivery"));
        btnEMI.setOnClickListener(v -> showPaymentConfirmation("EMI"));
        btnCreditCard.setOnClickListener(v -> showPaymentConfirmation("Credit Card"));
        btnDebitCard.setOnClickListener(v -> showPaymentConfirmation("Debit Card"));
        btnNetBanking.setOnClickListener(v -> showPaymentConfirmation("Net Banking"));
        btnUPI.setOnClickListener(v -> showPaymentConfirmation("UPI"));

        // Confirm Order button click listener
        btnConfirmOrder.setOnClickListener(v -> {
            // Add order confirmation logic here
            Toast.makeText(this, "Order Confirmed Successfully!", Toast.LENGTH_SHORT).show();
            // You can perform actions like sending the order details to a server here
        });
    }

    // Show payment confirmation
    private void showPaymentConfirmation(String paymentMethod) {
        Toast.makeText(this, "Payment through " + paymentMethod + " selected!", Toast.LENGTH_SHORT).show();
        // You can also implement further logic for processing the payment
    }
}
