package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RetailersPageActivity extends AppCompatActivity {

    private TextView shopDetailsTextView;
    private Button updateShopDetailsButton;

    // Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String SHOP_COLLECTION = "shops";
    private static final String SHOP_ID = "shop1"; // You can replace this with dynamic ID if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_page);

        shopDetailsTextView = findViewById(R.id.shopDetailsTextView);
        updateShopDetailsButton = findViewById(R.id.updateShopDetailsButton);

        // Load shop details when the activity is created
        loadShopDetails();

        // Button to navigate to update shop details activity
        updateShopDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open UpdateShopDetailsActivity to update shop details
                Intent intent = new Intent(RetailersPageActivity.this, UpdateShopDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Refresh shop details when the activity is visible again
        loadShopDetails();
    }

    private void loadShopDetails() {
        // Fetch the shop details from Firestore
        db.collection(SHOP_COLLECTION).document(SHOP_ID)
                .get() // Fetch the document by ID (e.g., "shop1")
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve shop details from Firestore document
                            String shopName = documentSnapshot.getString("shopName");
                            String ownerName = documentSnapshot.getString("ownerName");
                            String contactNo = documentSnapshot.getString("contactNo");
                            String address = documentSnapshot.getString("address");
                            String items = documentSnapshot.getString("items");

                            // Build a string to display the shop details
                            String details = "Shop Name: " + shopName + "\n"
                                    + "Owner: " + ownerName + "\n"
                                    + "Contact: " + contactNo + "\n"
                                    + "Address: " + address + "\n"
                                    + "Items: " + items;

                            // Display shop details on the TextView
                            shopDetailsTextView.setText(details);
                        } else {
                            // No details available for the shop
                            shopDetailsTextView.setText("No shop details available.");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur during the Firestore fetch operation
                    Toast.makeText(RetailersPageActivity.this, "Error loading shop details", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", e.getMessage());
                });
    }
}
