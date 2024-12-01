package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EditText searchInput;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if the user is authenticated
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // If the user is not logged in, navigate to the LoginActivity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
            return; // Skip the rest of the code
        }

        // Initialize the RecyclerView and the search input
        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.searchInput);

        // Load products from Firestore
        loadProducts();

        // Set the adapter to the RecyclerView
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Listen for text changes in the search input to filter products
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterProducts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        // Handle logout button click
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();
            // Redirect to LoginActivity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
        });
    }

    // Load products into the list from Firestore
    // Load products into the list from Firestore
    private void loadProducts() {
        productList.clear();

        // Reference to your Firestore collection for products
        CollectionReference productsRef = db.collection("shops")
                .document("shop1") // Specify the shop ID here
                .collection("items");

        // Reference to the shop document to get the shop name
        DocumentReference shopRef = db.collection("shops").document("shop1");

        // Fetch the shopName from the shop document
        shopRef.get().addOnCompleteListener(shopTask -> {
            if (shopTask.isSuccessful()) {
                DocumentSnapshot shopDocument = shopTask.getResult();
                if (shopDocument != null && shopDocument.exists()) {
                    // Retrieve the shop name from the shop document
                    String shopName = shopDocument.getString("shopName");

                    // Now fetch the products and add them to the list
                    productsRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    String itemName = document.getString("itemName");
                                    String itemCost = document.getString("itemCost");

                                    double cost = 0.0;
                                    try {
                                        cost = Double.parseDouble(itemCost); // Convert to double
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        cost = 0.0; // Default to 0 if parsing fails
                                    }

                                    // Add the product to the list with the shop name
                                    productList.add(new Product(itemName, cost, shopName));
                                }
                                productAdapter.notifyDataSetChanged(); // Update the adapter with the fetched products
                            }
                        } else {
                            Log.w("MainActivity", "Error getting products.", task.getException());
                        }
                    });
                } else {
                    Log.w("MainActivity", "Shop document does not exist.");
                }
            } else {
                Log.w("MainActivity", "Error getting shop info.", shopTask.getException());
            }
        });
    }



    // Filter products based on the search query
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        if (TextUtils.isEmpty(query)) {
            filteredList = productList; // If no search query, show all products
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.updateProductList(filteredList); // Update the adapter with filtered list
    }
}
