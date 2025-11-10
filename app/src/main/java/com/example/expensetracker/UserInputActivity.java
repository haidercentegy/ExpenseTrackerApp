package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserInputActivity extends AppCompatActivity {

    EditText nameInput;
    Spinner monthInput;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        nameInput = findViewById(R.id.nameInput);
        monthInput = findViewById(R.id.monthInput);
        btnContinue = findViewById(R.id.btnContinue);

        // --- Setup Spinner with months ---
        String[] months = new DateFormatSymbols().getMonths();
        List<String> monthList = new ArrayList<>();
        monthList.add("Select Month"); // placeholder

        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH); // 0-based
        for (int i = 0; i < months.length; i++) {
            monthList.add(months[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthInput.setAdapter(adapter);

        // Set current month as selected (skip placeholder at index 0)
        monthInput.setSelection(currentMonthIndex + 1);

        // --- Button click ---
        btnContinue.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String month = monthInput.getSelectedItem().toString();

            // Name validation
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

            // Month validation: ensure user didn't leave placeholder
            if (month.equals("Select Month")) {
                Toast.makeText(this, "Please select a month", Toast.LENGTH_SHORT).show();
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
