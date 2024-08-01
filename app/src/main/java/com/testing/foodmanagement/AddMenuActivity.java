package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddMenuActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText editTextName, editTextCategory, editTextDescription, editTextPrice, editTextIngredients;
    private CheckBox checkBoxAvailable;
    private ImageView imageView;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        editTextName = findViewById(R.id.editTextName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextIngredients = findViewById(R.id.editTextIngredients);
        checkBoxAvailable = findViewById(R.id.checkBoxAvailable);
        imageView = findViewById(R.id.addFoodimage);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        Button buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToDatabase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addItemToDatabase() {
        String name = editTextName.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String ingredients = editTextIngredients.getText().toString().trim();
        boolean available = checkBoxAvailable.isChecked();

        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || priceString.isEmpty() || ingredients.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Save the data to your database here
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addFoodItem(new FoodItem(0, name, category, description, price, ingredients, available, imageUri.toString()));

        Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
