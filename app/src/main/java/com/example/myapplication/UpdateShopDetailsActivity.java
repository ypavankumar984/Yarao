package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateShopDetailsActivity extends AppCompatActivity {

    private EditText shopNameEditText, ownerNameEditText, contactNoEditText, addressEditText;
    private LinearLayout geoDetailsLayout, shopDetailsLayout, itemsContainer;
    private Button updateGeoDetailsButton, updateItemsButton, addItemButton, saveAccountButton;

    // Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String SHOP_COLLECTION = "shops";
    private static final String SHOP_ID = "shop1"; // Replace with dynamic ID if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop_details);

        // Initialize views
        shopNameEditText = findViewById(R.id.shopNameEditText);
        ownerNameEditText = findViewById(R.id.ownerNameEditText);
        contactNoEditText = findViewById(R.id.contactNoEditText);
        addressEditText = findViewById(R.id.addressEditText);
        geoDetailsLayout = findViewById(R.id.geoDetailsLayout);
        shopDetailsLayout = findViewById(R.id.shopDetailsLayout);
        itemsContainer = findViewById(R.id.itemsContainer);

        updateGeoDetailsButton = findViewById(R.id.updateGeoDetailsButton);
        updateItemsButton = findViewById(R.id.updateItemsButton);
        addItemButton = findViewById(R.id.addItemButton);
        saveAccountButton = findViewById(R.id.saveButton); // New button to save account details

        // Initially hide the details and buttons
        geoDetailsLayout.setVisibility(View.GONE);
        shopDetailsLayout.setVisibility(View.GONE);
        itemsContainer.setVisibility(View.GONE);

        // Show Geo Details fields
        // Show Geo Details fields
        updateGeoDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoDetailsLayout.setVisibility(View.VISIBLE);
                shopDetailsLayout.setVisibility(View.GONE);
                itemsContainer.setVisibility(View.GONE); // Ensure items container is hidden
                saveAccountButton.setVisibility(View.VISIBLE); // Show Save button for account details
            }
        });

// Show Shop Details fields (Items section)
        // Show Shop Details fields (Items section)
        updateItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoDetailsLayout.setVisibility(View.GONE); // Hide geo details layout
                shopDetailsLayout.setVisibility(View.GONE); // Hide the shop details layout (if not needed)
                itemsContainer.setVisibility(View.VISIBLE); // Show the items container (which holds item cost fields)
                saveAccountButton.setVisibility(View.VISIBLE); // Show Save button for account details
            }
        });


// Add item dynamically
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemField(); // Add item name and cost fields
            }
        });

// Save Account details (Geo + Items)
        saveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGeoDetails();  // Save Geo details
                saveItemDetails(); // Save Item details
            }
        });

    }

    // Add new item input fields dynamically
    private void addItemField() {
        // Create a horizontal LinearLayout to hold item and cost
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create the Item name EditText
        EditText itemEditText = new EditText(this);
        itemEditText.setHint("Item Name");
        itemLayout.addView(itemEditText);

        // Create the Item cost EditText
        EditText costEditText = new EditText(this);
        costEditText.setHint("Item Cost");
        itemLayout.addView(costEditText);

        // Add the item layout to the container
        itemsContainer.addView(itemLayout);

        // Show "Add New Item" button after the first item is added
        if (itemsContainer.getChildCount() == 1) {
            addItemButton.setVisibility(View.VISIBLE);
        }
    }

    // Save Geo details to Firestore
    private void saveGeoDetails() {
        String shopName = shopNameEditText.getText().toString().trim();
        String ownerName = ownerNameEditText.getText().toString().trim();
        String contactNo = contactNoEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Debugging: Log the values to verify if the inputs are correct
        Log.d("GeoDetails", "Shop Name: " + shopName);
        Log.d("GeoDetails", "Owner Name: " + ownerName);
        Log.d("GeoDetails", "Contact No: " + contactNo);
        Log.d("GeoDetails", "Address: " + address);

        // Validate if any of the fields are empty
        if (TextUtils.isEmpty(shopName) || TextUtils.isEmpty(ownerName) || TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect geo-related details
        Map<String, Object> geoDetails = new HashMap<>();
        geoDetails.put("shopName", shopName);
        geoDetails.put("ownerName", ownerName);
        geoDetails.put("contactNo", contactNo);
        geoDetails.put("address", address);

        // Save to Firestore
        db.collection(SHOP_COLLECTION).document(SHOP_ID)
                .set(geoDetails)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(UpdateShopDetailsActivity.this, "Geo details updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateShopDetailsActivity.this, "Error updating geo details", Toast.LENGTH_SHORT).show();
                });
    }


    // Save Item details to Firestore
    private void saveItemDetails() {
        boolean allItemsValid = true;

        // Iterate over the dynamically added item fields
        for (int i = 0; i < itemsContainer.getChildCount(); i++) {
            LinearLayout itemLayout = (LinearLayout) itemsContainer.getChildAt(i);
            EditText itemEditText = (EditText) itemLayout.getChildAt(0); // Item name EditText
            EditText costEditText = (EditText) itemLayout.getChildAt(1); // Item cost EditText

            String itemName = itemEditText.getText().toString().trim();
            String itemCost = costEditText.getText().toString().trim();

            if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemCost)) {
                allItemsValid = false;
                break;
            }

            // Collect item-related details
            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("itemName", itemName);
            itemDetails.put("itemCost", itemCost);

            // Save to Firestore (you can save it in a subcollection or modify as needed)
            db.collection(SHOP_COLLECTION).document(SHOP_ID)
                    .collection("items") // Subcollection for items
                    .add(itemDetails)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(UpdateShopDetailsActivity.this, "Item details updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UpdateShopDetailsActivity.this, "Error updating item details", Toast.LENGTH_SHORT).show();
                    });
        }

        if (!allItemsValid) {
            Toast.makeText(this, "Please fill all item fields before saving", Toast.LENGTH_SHORT).show();
        }
    }
}
