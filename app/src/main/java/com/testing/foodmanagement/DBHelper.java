package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Canteen.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE Users(" +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "email TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "phoneNo TEXT, " +
                "address TEXT)"); // Added address column

        MyDB.execSQL("CREATE TABLE Orders(" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "itemName TEXT, " +
                "itemPrice TEXT, " +
                "itemQuantity TEXT)");

        MyDB.execSQL("CREATE TABLE finalOrder(" +
                "cur_date DATE DEFAULT CURRENT_DATE, " +
                "cur_time TIME DEFAULT CURRENT_TIME, " +
                "email TEXT, " +
                "orderName TEXT, " +
                "orderQuantity TEXT, " +
                "orderPrice TEXT)");
        MyDB.execSQL("CREATE TABLE food_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "category TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "ingredients TEXT, " +
                "available INTEGER, " +
                "image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS Users");
        MyDB.execSQL("DROP TABLE IF EXISTS Orders");
        MyDB.execSQL("DROP TABLE IF EXISTS finalOrder");
        MyDB.execSQL("DROP TABLE IF EXISTS food_items");
        onCreate(MyDB); // Recreate the tables after dropping
    }

    public void addFoodItem(FoodItem foodItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", foodItem.getName());
        values.put("category", foodItem.getCategory());
        values.put("description", foodItem.getDescription());
        values.put("price", foodItem.getPrice());
        values.put("ingredients", foodItem.getIngredients());
        values.put("available", foodItem.isAvailable() ? 1 : 0);
        values.put("image", foodItem.getImageUri());  // Assuming you store image URIs/paths as strings

        db.insert("food_items", null, values);
        db.close();
    }
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, ingredients, available, imageUri);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }


    public boolean insertData(String firstName, String lastName, String email, String password, String phoneNo, String address) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phoneNo", phoneNo);
        contentValues.put("address", address); // Added address to content values
        long result = MyDB.insert("Users", null, contentValues);
        return result != -1; // Return true if insert was successful
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
        return cursor.getCount() > 0; // Return true if user exists
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=? AND password=?", new String[]{email, password});
        return cursor.getCount() > 0; // Return true if email and password match
    }

    public Cursor getData(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
    }
}
