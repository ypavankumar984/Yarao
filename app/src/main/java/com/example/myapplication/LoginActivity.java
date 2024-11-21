package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find views by ID
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is already logged in, navigate to the appropriate page
            checkUserRoleAndNavigate(currentUser.getEmail());
        }

        // Login button functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    // Check user in Firestore first
                    checkUserInFirestore(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navigate to Sign-Up page
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    // Check if the user exists in Firestore and authenticate
    private void checkUserInFirestore(final String email, final String password) {
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Fetch the role of the user from Firestore
                        selectedRole = document.getString("role");

                        // Proceed with authentication if the role exists
                        if (selectedRole != null) {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if (user != null && user.isEmailVerified()) {
                                                    // Redirect based on the role
                                                    checkUserRoleAndNavigate(email);
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Account not verified.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Role not found in Firestore (Unexpected scenario)
                            Toast.makeText(LoginActivity.this, "Error fetching role information.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User does not exist in Firestore
                        Toast.makeText(LoginActivity.this, "Account does not exist with this email.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error retrieving user data from Firestore
                    Toast.makeText(LoginActivity.this, "Error checking user in Firestore.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Check the user role and navigate accordingly
    private void checkUserRoleAndNavigate(String email) {
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        selectedRole = document.getString("role");

                        if (selectedRole != null) {
                            // Redirect based on the role
                            if (selectedRole.equals("Retailer")) {
                                // Navigate to Retailers Page
                                Intent retailerIntent = new Intent(LoginActivity.this, RetailersPageActivity.class);
                                startActivity(retailerIntent);
                                finish(); // Close the login activity
                            } else {
                                // Navigate to MainActivity for other roles (like user)
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish(); // Close the login activity
                            }
                        }
                    }
                }
            }
        });
    }
}
