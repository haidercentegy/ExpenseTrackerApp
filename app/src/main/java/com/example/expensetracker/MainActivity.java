package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvWelcome, tvMonth, tvTotalExpense;
    Button btnAddExpense, btnViewSummary;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvMonth = findViewById(R.id.tvMonth);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnViewSummary = findViewById(R.id.btnViewSummary);

        dbHelper = new DatabaseHelper(this);

        String name = getIntent().getStringExtra("userName");
        String month = getIntent().getStringExtra("month");

        tvWelcome.setText("Welcome, " + (name != null ? name : "User") + "!");
        tvMonth.setText("Month: " + (month != null ? month : "November"));
        tvTotalExpense.setText("Rs. " + dbHelper.getTotalExpense());

        btnAddExpense.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddExpenseActivity.class))
        );

        btnViewSummary.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SummaryActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh total expense
        tvTotalExpense.setText("Rs. " + dbHelper.getTotalExpense());
    }

    // ✅ Exit Confirmation Dialog on Back Press
    @Override
    public void onBackPressed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    super.onBackPressed(); // ✅ now properly calling super
                    finishAffinity();      // closes entire app
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
