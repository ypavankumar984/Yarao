package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CourierPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_page);

        // Handle top-right button click
        Button topRightButton = findViewById(R.id.top_right_button);
        topRightButton.setOnClickListener(v -> {
            Toast.makeText(CourierPageActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
        });

        // Handle logout button click
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            // Log out the user
            FirebaseAuth.getInstance().signOut();

            // Redirect to LoginActivity
            Intent loginIntent = new Intent(CourierPageActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
        });
    }
}
