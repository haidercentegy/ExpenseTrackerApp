package com.example.expensetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Insert new expense
    public void addExpense(String title, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);

        // Auto-fill current date
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        values.put(COLUMN_DATE, currentDate);

        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    // Fetch all expenses as ExpenseItem objects
    public List<ExpenseItem> getAllExpenseItems() {
        List<ExpenseItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));

                list.add(new ExpenseItem(id, title, amount, category, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Update existing expense
    public void updateExpense(int id, String title, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        db.update(TABLE_EXPENSES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete a specific expense by ID
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get total expense
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_EXPENSES, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // Clear all expenses
    public void clearAllExpenses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXPENSES);
        db.close();
    }

    // Inner class to hold expense details
    public static class ExpenseItem {
        public int id;
        public String title;
        public double amount;
        public String category;
        public String date;

        public ExpenseItem(int id, String title, double amount, String category, String date) {
            this.id = id;
            this.title = title;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        @Override
        public String toString() {
            return title + " - Rs." + amount + " (" + category + ", " + date + ")";
        }
    }
}
