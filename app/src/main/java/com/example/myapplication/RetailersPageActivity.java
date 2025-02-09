package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RetailersPageActivity extends AppCompatActivity {

    private TextView shopNameTextView, ownerNameTextView, contactNoTextView, addressTextView;
    private Button refreshButton, addShopButton, saveShopButton, addItemButton, saveItemButton, logoutButton;
    private LinearLayout addShopLayout, addItemLayout;
    private EditText shopNameInput, ownerNameInput, contactNoInput, addressInput, itemNameInput, itemCostInput;
    private RecyclerView itemsRecyclerView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth; // Firebase Authentication
    private static final String SHOP_COLLECTION = "shops";
    private static final String ITEMS_COLLECTION = "items";

    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_page);

        // Initialize views
        shopNameTextView = findViewById(R.id.shopNameTextView);
        ownerNameTextView = findViewById(R.id.ownerNameTextView);
        contactNoTextView = findViewById(R.id.contactNoTextView);
        addressTextView = findViewById(R.id.addressTextView);
        refreshButton = findViewById(R.id.refreshButton);
        addShopButton = findViewById(R.id.addShopButton);
        saveShopButton = findViewById(R.id.saveShopButton);
        addItemButton = findViewById(R.id.addItemButton);
        saveItemButton = findViewById(R.id.saveItemButton);
        logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button
        addShopLayout = findViewById(R.id.addShopLayout);
        addItemLayout = findViewById(R.id.addItemLayout);
        shopNameInput = findViewById(R.id.shopNameInput);
        ownerNameInput = findViewById(R.id.ownerNameInput);
        contactNoInput = findViewById(R.id.contactNoInput);
        addressInput = findViewById(R.id.addressInput);
        itemNameInput = findViewById(R.id.itemNameInput);
        itemCostInput = findViewById(R.id.itemCostInput);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Firebase Authentication

        // Initialize the item list and adapter
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemAdapter);

        // Fetch the shop details and items from Firestore using the logged-in user's email
        fetchShopDetails();

        // Set onClick listeners
        addShopButton.setOnClickListener(view -> addShopLayout.setVisibility(View.VISIBLE));
        addItemButton.setOnClickListener(view -> addItemLayout.setVisibility(View.VISIBLE));

        saveShopButton.setOnClickListener(view -> saveShopDetails());
        saveItemButton.setOnClickListener(view -> saveItemDetails());

        // Refresh Button
        refreshButton.setOnClickListener(view -> fetchShopDetails());

        // Logout Button
        logoutButton.setOnClickListener(view -> logoutUser());
    }

    private void fetchShopDetails() {
        String userEmail = mAuth.getCurrentUser().getEmail(); // Get the logged-in user's email

        if (userEmail == null) {
            Toast.makeText(RetailersPageActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the user's email as the shop ID to fetch details
        db.collection(SHOP_COLLECTION).document(userEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String shopName = documentSnapshot.getString("shopName");
                        String ownerName = documentSnapshot.getString("ownerName");
                        String contactNo = documentSnapshot.getString("contactNo");
                        String address = documentSnapshot.getString("address");

                        // Display the details in the TextViews
                        shopNameTextView.setText("Shop Name: " + shopName);
                        ownerNameTextView.setText("Owner Name: " + ownerName);
                        contactNoTextView.setText("Contact No: " + contactNo);
                        addressTextView.setText("Address: " + address);

                        // Fetch items for this shop using the email as the shop ID
                        fetchItems(userEmail);
                    } else {
                        Toast.makeText(RetailersPageActivity.this, "No shop details found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RetailersPageActivity.this, "Error fetching shop details", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchItems(String userEmail) {
        db.collection(SHOP_COLLECTION).document(userEmail)
                .collection(ITEMS_COLLECTION)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    itemList.clear(); // Clear the list before adding new data
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String itemName = document.getString("itemName");
                        double itemCost = document.getDouble("itemCost"); // Fetch as double
                        itemList.add(new Item(itemName, itemCost)); // Add item to list
                    }
                    itemAdapter.notifyDataSetChanged(); // Notify adapter to update RecyclerView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RetailersPageActivity.this, "Error fetching items", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveShopDetails() {
        String shopName = shopNameInput.getText().toString();
        String ownerName = ownerNameInput.getText().toString();
        String contactNo = contactNoInput.getText().toString();
        String address = addressInput.getText().toString();

        if (shopName.isEmpty() || ownerName.isEmpty() || contactNo.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the logged-in user's email as the shop ID
        String userEmail = mAuth.getCurrentUser().getEmail();
        if (userEmail != null) {
            // Create a shop document using the user's email as the document ID
            db.collection(SHOP_COLLECTION).document(userEmail)
                    .set(new Shop(shopName, ownerName, contactNo, address))
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(RetailersPageActivity.this, "Shop details saved", Toast.LENGTH_SHORT).show();
                        addShopLayout.setVisibility(View.GONE); // Hide the input fields
                        fetchShopDetails(); // Refresh shop and item data
                    })
                    .addOnFailureListener(e -> Toast.makeText(RetailersPageActivity.this, "Error saving shop details", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveItemDetails() {
        String itemName = itemNameInput.getText().toString();
        String itemCostString = itemCostInput.getText().toString();

        if (itemName.isEmpty() || itemCostString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double itemCost = Double.parseDouble(itemCostString); // Parse item cost to double

            // Get the logged-in user's email as the shop ID
            String userEmail = mAuth.getCurrentUser().getEmail();
            if (userEmail != null) {
                // Create an item document
                db.collection(SHOP_COLLECTION).document(userEmail)
                        .collection(ITEMS_COLLECTION)
                        .add(new Item(itemName, itemCost)) // Pass the double value
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(RetailersPageActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
                            addItemLayout.setVisibility(View.GONE); // Hide the input fields
                            fetchItems(userEmail); // Refresh item list
                        })
                        .addOnFailureListener(e -> Toast.makeText(RetailersPageActivity.this, "Error saving item", Toast.LENGTH_SHORT).show());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid cost format. Please enter a valid number.", Toast.LENGTH_SHORT).show();
        }
    }


    // Logout function
    private void logoutUser() {
        mAuth.signOut(); // Sign out from Firebase
        Intent intent = new Intent(RetailersPageActivity.this, LoginActivity.class); // Redirect to LoginActivity
        startActivity(intent);
        finish(); // Close the current activity
    }
}
