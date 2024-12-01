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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
            finish();
            return;
        }

        // Initialize the RecyclerView and search input
        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.searchInput);

        // Load products from Firestore
        loadProducts();

        // Set up the RecyclerView adapter
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Listen for search input changes to filter products
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Handle logout button click
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out the user
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }

    // Load products into the list from Firestore
    private void loadProducts() {
        productList.clear();

        // Reference to the "shops" collection
        CollectionReference shopsRef = db.collection("shops");

        // Fetch all shop documents
        shopsRef.get().addOnCompleteListener(shopsTask -> {
            if (shopsTask.isSuccessful()) {
                QuerySnapshot shopsSnapshot = shopsTask.getResult();
                if (shopsSnapshot != null) {
                    for (DocumentSnapshot shopDocument : shopsSnapshot.getDocuments()) {
                        String shopName = shopDocument.getString("shopName"); // Shop name
                        String shopId = shopDocument.getId(); // Shop ID

                        // Reference to the "items" collection for each shop
                        CollectionReference itemsRef = shopDocument.getReference().collection("items");

                        // Fetch all items in the current shop
                        itemsRef.get().addOnCompleteListener(itemsTask -> {
                            if (itemsTask.isSuccessful()) {
                                QuerySnapshot itemsSnapshot = itemsTask.getResult();
                                if (itemsSnapshot != null) {
                                    for (DocumentSnapshot itemDocument : itemsSnapshot.getDocuments()) {
                                        String itemName = itemDocument.getString("itemName");
                                        double itemCost = itemDocument.getDouble("itemCost"); // Get itemCost as double

                                        // Add product to the list
                                        productList.add(new Product(itemName, itemCost, shopName));
                                    }
                                    productAdapter.notifyDataSetChanged(); // Notify adapter
                                }
                            } else {
                                Log.w("MainActivity", "Error fetching items for shop: " + shopId, itemsTask.getException());
                            }
                        });
                    }
                } else {
                    Log.w("MainActivity", "No shops found in Firestore.");
                }
            } else {
                Log.w("MainActivity", "Error fetching shops.", shopsTask.getException());
            }
        });
    }

    // Filter products based on search query
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
        productAdapter.updateProductList(filteredList); // Update adapter with filtered list
    }
}
