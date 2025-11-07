package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    TextView totalExpenseText;
    ListView expenseListView;
    Button btnBack;
    Spinner categorySpinner;
    DatabaseHelper dbHelper;
    ArrayAdapter<DatabaseHelper.ExpenseItem> adapter;
    List<DatabaseHelper.ExpenseItem> expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        totalExpenseText = findViewById(R.id.totalExpenseText);
        expenseListView = findViewById(R.id.expenseListView);
        btnBack = findViewById(R.id.btnBack);
        categorySpinner = findViewById(R.id.categorySpinner);

        dbHelper = new DatabaseHelper(this);

        // Fetch all expenses
        expenses = dbHelper.getAllExpenseItems();

        // Show total expense
        totalExpenseText.setText("Total Spent: Rs. " + dbHelper.getTotalExpense());

        // Adapter to display expenses
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(expenses));
        expenseListView.setAdapter(adapter);

        // Long press to delete an expense
        expenseListView.setOnItemLongClickListener((parent, view, position, id) -> {
            DatabaseHelper.ExpenseItem item = (DatabaseHelper.ExpenseItem) adapter.getItem(position);

            new AlertDialog.Builder(SummaryActivity.this)
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?\n\n" + item.toString())
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteExpense(item.id);
                        expenses.remove(item);
                        adapter.remove(item);
                        adapter.notifyDataSetChanged();
                        updateTotalExpense();
                        updateCategorySpinner();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

        // Tap to edit an expense
        expenseListView.setOnItemClickListener((parent, view, position, id) -> {
            DatabaseHelper.ExpenseItem item = (DatabaseHelper.ExpenseItem) adapter.getItem(position);
            Intent intent = new Intent(SummaryActivity.this, AddExpenseActivity.class);
            intent.putExtra("expenseId", item.id);
            intent.putExtra("title", item.title);
            intent.putExtra("amount", item.amount);
            intent.putExtra("category", item.category);
            startActivity(intent);
        });

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Category filter
        setupCategorySpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh after edit
        expenses = dbHelper.getAllExpenseItems();
        adapter.clear();
        adapter.addAll(expenses);
        adapter.notifyDataSetChanged();
        updateTotalExpense();
        updateCategorySpinner();
    }

    // Update total expense display
    private void updateTotalExpense() {
        totalExpenseText.setText("Total Spent: Rs. " + dbHelper.getTotalExpense());
    }

    // Setup spinner with categories
    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All"); // default
        for (DatabaseHelper.ExpenseItem e : expenses) {
            if (!categories.contains(e.category)) categories.add(e.category);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = categories.get(position);
                List<DatabaseHelper.ExpenseItem> filtered = new ArrayList<>();
                if (selected.equals("All")) {
                    filtered.addAll(expenses);
                } else {
                    for (DatabaseHelper.ExpenseItem e : expenses) {
                        if (e.category.equals(selected)) filtered.add(e);
                    }
                }
                adapter.clear();
                adapter.addAll(filtered);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Update spinner options (after delete)
    private void updateCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (DatabaseHelper.ExpenseItem e : expenses) {
            if (!categories.contains(e.category)) categories.add(e.category);
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
    }
}
