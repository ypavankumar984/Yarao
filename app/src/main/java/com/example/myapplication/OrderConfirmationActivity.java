package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    private EditText editName, editAddress;
    private Button btnConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize the views
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Handle Confirm Order button click
        btnConfirmOrder.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String address = editAddress.getText().toString();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in both name and address.", Toast.LENGTH_SHORT).show();
            } else {
                // Pass the name and address to the next activity (PaymentOptionsActivity)
                Intent intent = new Intent(OrderConfirmationActivity.this, PaymentOptionsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                startActivity(intent);
            }
        });
    }
}
