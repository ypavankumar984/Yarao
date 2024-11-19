package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView and the search input
        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.searchInput);

        // Load the static product list for homepage
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
    }

    // Load products into the list
    private void loadProducts() {
        productList.add(new Product("Shirt", "$10.99"));
        productList.add(new Product("Shoes", "$25.99"));
        productList.add(new Product("Pants", "$30.99"));
        productList.add(new Product("Watch", "$50.99"));
        Log.d("MainActivity", "Static Products: " + productList.size());
    }

    // Filter products based on the search query
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        if (TextUtils.isEmpty(query)) {
            filteredList = productList;
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.updateProductList(filteredList);
    }
}
