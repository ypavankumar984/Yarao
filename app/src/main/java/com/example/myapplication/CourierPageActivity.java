package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CourierPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_page);

        // Handle button click
        Button topRightButton = findViewById(R.id.top_right_button);
        topRightButton.setOnClickListener(v -> {
            Toast.makeText(CourierPageActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
        });
    }
}
