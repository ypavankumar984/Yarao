package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the product item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // Bind the product data to the views
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());

        // Set up the "Buy" button click listener
        holder.buyButton.setOnClickListener(view -> {
            // Create an intent to start the OrderConfirmationActivity
            Intent intent = new Intent(view.getContext(), OrderConfirmationActivity.class);
            // Pass the product details to the new activity
            intent.putExtra("productName", product.getName());
            intent.putExtra("productPrice", product.getPrice());
            // Start the OrderConfirmationActivity
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list
    public void updateProductList(List<Product> newProductList) {
        productList = newProductList;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    // ViewHolder class for each product item
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        Button buyButton;  // Reference to the Buy button

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            buyButton = itemView.findViewById(R.id.buyButton);  // Initialize the Buy button
        }
    }
}
