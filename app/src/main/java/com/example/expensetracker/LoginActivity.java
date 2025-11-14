package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnSignup;
    DatabaseHelper db;

    // ✅ Hardcoded credentials
    private static final String TEST_USERNAME = "haider";
    private static final String TEST_PASSWORD = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        // Login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "⚠️ Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Hardcoded login check
            if (username.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {
                Toast.makeText(this, "🎉 Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userName", username); // pass username
                startActivity(intent);
                finish();
            }
            // Optional: check database for real users
            else if (db.checkUser(username, password)) {
                Toast.makeText(this, "🎉 Login Successful (DB)!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userName", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "❌ Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Sign Up button
        btnSignup.setOnClickListener(v -> {
            // Redirect to User Input / Sign Up screen
//            Intent intent = new Intent(this, UserInputActivity.class);
//            startActivity(intent);
        });
    }
}
