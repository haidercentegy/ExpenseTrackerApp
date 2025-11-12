package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "expenseTracker.db";
    private static final int DB_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Expenses table
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COL_EXP_ID = "id";
    private static final String COL_EXP_TITLE = "title";
    private static final String COL_EXP_AMOUNT = "amount";
    private static final String COL_EXP_CATEGORY = "category";
    private static final String COL_EXP_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        String createExpenseTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COL_EXP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EXP_TITLE + " TEXT," +
                COL_EXP_AMOUNT + " REAL," +
                COL_EXP_CATEGORY + " TEXT," +
                COL_EXP_DATE + " TEXT)";
        db.execSQL(createExpenseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // ------------------ USER METHODS ------------------

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, cv);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_ID},
                COL_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ------------------ EXPENSE METHODS ------------------

    public boolean addExpense(String title, double amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXP_TITLE, title);
        cv.put(COL_EXP_AMOUNT, amount);
        cv.put(COL_EXP_CATEGORY, category);
        cv.put(COL_EXP_DATE, date);
        long result = db.insert(TABLE_EXPENSES, null, cv);
        db.close();
        return result != -1;
    }

    public boolean updateExpense(int id, String title, double amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXP_TITLE, title);
        cv.put(COL_EXP_AMOUNT, amount);
        cv.put(COL_EXP_CATEGORY, category);
        cv.put(COL_EXP_DATE, date);
        int result = db.update(TABLE_EXPENSES, cv, COL_EXP_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EXPENSES, COL_EXP_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public double getTotalExpense() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_EXP_AMOUNT + ") FROM " + TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        db.close();
        return total;
    }

    // ✅ Return a List of ExpenseItem for SummaryActivity
    public List<ExpenseItem> getAllExpenseItems() {
        List<ExpenseItem> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES,
                null, null, null, null, null,
                COL_EXP_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXP_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_DATE));

                expenseList.add(new ExpenseItem(id, title, amount, category, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // ------------------ EXPENSE ITEM MODEL ------------------
    public static class ExpenseItem {
        public int id;
        public String title;
        public String category;
        public double amount;
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
            return category + " - " + title + ": Rs. " + amount + " on " + date;
        }
    }
}
