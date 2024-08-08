package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Canteen.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        checkDatabase();
        MyDB.execSQL("CREATE TABLE Users(" +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "email TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "phoneNo TEXT, " +
                "address TEXT, " +
                "profile_image BLOB)");

        MyDB.execSQL("CREATE TABLE Orders(" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "itemName TEXT, " +
                "itemPrice TEXT, " +
                "itemQuantity TEXT,"+
                "branch TEXT)");

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
                "image BLOB)");

        MyDB.execSQL("CREATE TABLE Branches(" +
                "branchName TEXT PRIMARY KEY, " +
                "phone TEXT, " +
                "email TEXT, " +
                "openHours TEXT, " +
                "location TEXT)");

        // Add the new Category table creation
        MyDB.execSQL("CREATE TABLE Categories(" +
                "categoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoryName TEXT, " +
                "categoryImage BLOB)");
        MyDB.execSQL("CREATE TABLE Customizations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL)");

        MyDB.execSQL("CREATE TABLE FoodCustomizations(" +
                "foodItemId INTEGER, " +
                "customizationId INTEGER, " +
                "FOREIGN KEY(foodItemId) REFERENCES food_items(id), " +
                "FOREIGN KEY(customizationId) REFERENCES Customizations(id))");


        Log.d("DBHelper", "Database tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS Users");
        MyDB.execSQL("DROP TABLE IF EXISTS Orders");
        MyDB.execSQL("DROP TABLE IF EXISTS finalOrder");
        MyDB.execSQL("DROP TABLE IF EXISTS food_items");
        MyDB.execSQL("DROP TABLE IF EXISTS Branches");
        MyDB.execSQL("DROP TABLE IF EXISTS Categories");
        MyDB.execSQL("DROP TABLE IF EXISTS Customizations");
        MyDB.execSQL("DROP TABLE IF EXISTS FoodCustomizations");
        onCreate(MyDB);
    }
    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryName", category.getCategoryName());
        values.put("categoryImage", category.getCategoryImage()); // Store image as byte array

        long result = db.insert("Categories", null, values);
        db.close();

    }
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                @SuppressLint("Range") byte[] categoryImage = cursor.getBlob(cursor.getColumnIndex("categoryImage"));

                Category category = new Category(categoryId, categoryName, categoryImage);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public int addFoodItem(FoodItem foodItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", foodItem.getName());
        values.put("category", foodItem.getCategory());
        values.put("description", foodItem.getDescription());
        values.put("price", foodItem.getPrice());
        values.put("ingredients", foodItem.getIngredients());
        values.put("available", foodItem.isAvailable() ? 1 : 0);
        values.put("image", foodItem.getImage());  // Store image as byte array

        int foodId = (int) db.insert("food_items", null, values);
        db.close();
        return foodId;
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
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, ingredients, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public boolean insertData(String firstName, String lastName, String email, String password, String phoneNo, String address, byte[] profileImage) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phoneNo", phoneNo);
        contentValues.put("address", address);
        contentValues.put("profile_image", profileImage);
        long result = MyDB.insert("Users", null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=? AND password=?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public byte[] getUserProfileImage(String email) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT profile_image FROM Users WHERE email=?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(0);
            cursor.close();
            return image;
        }
        return null;
    }

    public Cursor getData(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
    }

    public boolean addBranch(Branch branch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("branchName", branch.getBranchName());
        values.put("phone", branch.getPhone());
        values.put("email", branch.getEmail());
        values.put("openHours", branch.getOpenHours());
        values.put("location", branch.getLocation());

        long result = db.insert("Branches", null, values);
        db.close();
        return result != -1;
    }

    public List<Branch> getAllBranches() {
        List<Branch> branchList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Branches", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String branchName = cursor.getString(cursor.getColumnIndex("branchName"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String openHours = cursor.getString(cursor.getColumnIndex("openHours"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));

                Branch branch = new Branch(branchName, phone, email, openHours, location);
                branchList.add(branch);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return branchList;
    }

    public List<FoodItem> getLastFiveAddedFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "food_items",
                null,
                null,
                null,
                null,
                null,
                "id DESC",
                "5"
        );
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, ingredients, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public List<FoodItem> getFoodItemsByCategory(String categoryName) {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE category = ?", new String[]{categoryName});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, ingredients, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }


    public List<FoodItem> getAvailableFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE available = 1", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, ingredients, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }
    public long addCustomization(Customization customization) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", customization.getName());
        values.put("price", customization.getPrice());

        long customizationId = db.insert("Customizations", null, values);
        db.close();
        return customizationId;
    }

    public List<Customization> getAllCustomizations() {
        List<Customization> customizations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customizations", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Customization customization = new Customization(id, name, price);
                customizations.add(customization);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customizations;
    }

    public void assignCustomizationsToFoodItem(int foodItemId, List<Integer> customizationIds) {
        Log.d("DBHelper", "assignCustomizationsToFoodItem called with foodItemId: " + foodItemId + " and customizationIds: " + customizationIds);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int customizationId : customizationIds) {
                ContentValues values = new ContentValues();
                values.put("foodItemId", foodItemId);
                values.put("customizationId", customizationId);
                long result = db.insert("FoodCustomizations", null, values);
                if (result == -1) {
                    Log.e("DBHelper", "Failed to insert customizationId " + customizationId + " for foodItemId " + foodItemId);
                } else {
                    Log.d("DBHelper", "Successfully inserted customizationId " + customizationId + " for foodItemId " + foodItemId);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void checkDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(FoodCustomizations)", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("DBHelper", "Column: " + columnName);
            }
            cursor.close();
        }
        db.close();
    }




    public List<Customization> getCustomizationsForFoodItem(int foodItemId) {
        List<Customization> customizations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT Customizations.id, Customizations.name, Customizations.price " +
                        "FROM Customizations " +
                        "INNER JOIN FoodCustomizations ON Customizations.id = FoodCustomizations.customizationId " +
                        "WHERE FoodCustomizations.foodItemId = ?", new String[]{String.valueOf(foodItemId)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Customization customization = new Customization(id, name, price);
                customizations.add(customization);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customizations;
    }
}