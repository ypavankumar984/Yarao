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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EditText searchInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

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

        // Load the product list
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

    // Load products into the list
    private void loadProducts() {
        productList.clear();

        // Static product list for general use
        productList.add(new Product("Shirt", "$10.99"));
        productList.add(new Product("Shoes", "$30.99"));
        productList.add(new Product("Watch", "$50.99"));
        productList.add(new Product("Pants", "$25.99"));
        productList.add(new Product("Delivery Bag", "$15.99"));
        productList.add(new Product("Helmet", "$25.99"));
        productList.add(new Product("GPS Device", "$35.99"));
        productList.add(new Product("Merchant Shoes", "$20.99"));
        productList.add(new Product("Inventory Organizer", "$55.99"));
        productList.add(new Product("Shop Sign", "$45.99"));
        productList.add(new Product("Packaging Materials", "$10.99"));
        // Add more products as needed
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
