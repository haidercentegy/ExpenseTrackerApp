package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etFullName, etUsername, etPassword;
    Button btnCreateAccount;
    TextView tvLoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Bind views
        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLoginNow = findViewById(R.id.tvLoginNow);

        // Create Account Button click
        btnCreateAccount.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validation
            if(name.isEmpty() || username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(password.length() < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Save data to database (DatabaseHelper)
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            // Optional: Go to LoginActivity after creating account
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Login Now click
        tvLoginNow.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
