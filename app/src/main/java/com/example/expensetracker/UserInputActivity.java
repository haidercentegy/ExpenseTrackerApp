package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserInputActivity extends AppCompatActivity {

    EditText nameInput, monthInput;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        nameInput = findViewById(R.id.nameInput);
        monthInput = findViewById(R.id.monthInput);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String month = monthInput.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameInput.setError("Please enter your name");
                nameInput.requestFocus();
                return;
            }
            if (!name.matches("[a-zA-Z ]+")) {
                nameInput.setError("Name should only contain letters");
                nameInput.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(month)) {
                monthInput.setError("Please enter the month");
                monthInput.requestFocus();
                return;
            }
            if (!month.matches("^[A-Za-z]+$")) {
                monthInput.setError("Invalid month name (letters only)");
                monthInput.requestFocus();
                return;
            }

            Toast.makeText(this, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserInputActivity.this, MainActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("month", month);
            startActivity(intent);
            finish();
        });
    }
}
