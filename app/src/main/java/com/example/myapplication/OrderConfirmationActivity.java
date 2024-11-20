package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        // Get the product details from the Intent
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");

        // Display the order confirmation message and product details
        orderMessageTextView.setText("Your Order Details!");
        productNameTextView.setText("Product: " + productName);
        productPriceTextView.setText("Price: " + productPrice);

        // Set up the payment options
        setUpPaymentMethods();
    }

    private void setUpPaymentMethods() {
        Button btnGPay = findViewById(R.id.btnGPay);
        Button btnPhonePe = findViewById(R.id.btnPhonePe);
        Button btnCOD = findViewById(R.id.btnCOD);

        // Set OnClickListener for each payment option
        btnGPay.setOnClickListener(v -> openGooglePay());
        btnPhonePe.setOnClickListener(v -> openPhonePe());
        btnCOD.setOnClickListener(v -> showCODConfirmation());
    }

    // Example for Google Pay Intent
    private void openGooglePay() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("upi://pay?pa=merchantId@bank&pn=MerchantName&mc=1234&tid=12345&tr=orderId123&tn=OrderPayment&am=10&cu=INR"));
            intent.setPackage("com.google.android.apps.nbu.paisa.user");
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Google Pay is not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    // Example for PhonePe Intent
    private void openPhonePe() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("upi://pay?pa=merchantId@bank&pn=MerchantName&mc=1234&tid=12345&tr=orderId123&tn=OrderPayment&am=10&cu=INR"));
            intent.setPackage("com.phonepe.app");
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            Toast.makeText(this, "PhonePe is not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    // Example for COD Confirmation
    private void showCODConfirmation() {
        Toast.makeText(this, "You chose Cash on Delivery. Pay upon receiving the order.", Toast.LENGTH_SHORT).show();
    }
}
