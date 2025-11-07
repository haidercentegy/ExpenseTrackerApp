package com.example.expensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    EditText etTitle, etAmount;
    Spinner spinnerCategory;
    Button btnSave;
    DatabaseHelper dbHelper;
    int expenseId = -1; // Track edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // ✅ Setup predefined categories
        String[] categories = {
                "Food",
                "Travel",
                "Education",
                "Bills & Utilities",
                "Health & Fitness",
                "Entertainment",
                "Shopping",
                "Rent",
                "Groceries",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // ✅ Check if this is Edit Mode
        Intent intent = getIntent();
        expenseId = intent.getIntExtra("expenseId", -1);

        if (expenseId != -1) {
            // Prefill fields for edit
            etTitle.setText(intent.getStringExtra("title"));
            etAmount.setText(String.valueOf(intent.getDoubleExtra("amount", 0)));

            // Set selected category in spinner
            String currentCategory = intent.getStringExtra("category");
            if (currentCategory != null) {
                int spinnerPosition = adapter.getPosition(currentCategory);
                if (spinnerPosition >= 0) spinnerCategory.setSelection(spinnerPosition);
            }
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim().replaceAll("\\s+", " ");
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString(); // ✅ get from spinner

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            if (expenseId == -1) {
                // ✅ Add New Expense
                dbHelper.addExpense(title, amount, category);
                Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
            } else {
                // ✅ Update Existing Expense
                dbHelper.updateExpense(expenseId, title, amount, category);
                Toast.makeText(this, "Expense Updated!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Go back to Summary or Main screen
        });
    }
}
