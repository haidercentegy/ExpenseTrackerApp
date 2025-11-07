package com.example.expensetracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseHomeActivity extends AppCompatActivity {

    TextView tvWelcome, tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvMonth = findViewById(R.id.tvMonth);

        // Get data from Intent
        String userName = getIntent().getStringExtra("userName");
        String month = getIntent().getStringExtra("month");

        tvWelcome.setText("Welcome, " + userName + "!");
        tvMonth.setText("Month: " + month);
    }
}
