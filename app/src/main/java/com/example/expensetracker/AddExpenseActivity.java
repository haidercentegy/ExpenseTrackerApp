package com.example.expensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    EditText etTitle, etAmount, etCategory;
    Button btnSave;
    DatabaseHelper dbHelper;
    int expenseId = -1; // Track edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // ✅ Check if this is Edit Mode
        Intent intent = getIntent();
        expenseId = intent.getIntExtra("expenseId", -1);

        if (expenseId != -1) {
            // Prefill fields for edit
            etTitle.setText(intent.getStringExtra("title"));
            etAmount.setText(String.valueOf(intent.getDoubleExtra("amount", 0)));
            etCategory.setText(intent.getStringExtra("category"));
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim().replaceAll("\\s+", " ");
            String amountStr = etAmount.getText().toString().trim();
            String category = etCategory.getText().toString().trim().replaceAll("\\s+", " ");

            if (title.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
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
