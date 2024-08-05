package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private LinearLayout linearLayoutRecentlyAdded;
    private CardView cardViewFoodItems;
    private EditText searchEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main2);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        linearLayoutRecentlyAdded = findViewById(R.id.linearLayoutRecentlyAdded);
        cardViewFoodItems = findViewById(R.id.cardViewFoodItems);
        searchEditText = findViewById(R.id.search_edit_text); // Make sure this matches your EditText ID

        displayRecentlyAddedItems();

        cardViewFoodItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, CustomerMenuActivity.class);
                startActivity(intent);
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = searchEditText.getText().toString();
                    performSearch(query);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker at a specific location and move the camera
        LatLng location = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        // Set a click listener on the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Open Google Maps with a specific location
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latLng.latitude + "," + latLng.longitude + "(Marker)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    private void performSearch(String query) {
        // Handle search logic here
        Toast.makeText(MainActivity2.this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
        // For example, start a new activity to show search results or filter the current list
    }

    private void displayRecentlyAddedItems() {
        DBHelper dbHelper = new DBHelper(this);
        List<FoodItem> recentItems = dbHelper.getLastFiveAddedFoodItems(); // Method to get the last 5 items

        LayoutInflater inflater = LayoutInflater.from(this);
        for (FoodItem foodItem : recentItems) {
            View foodItemView = inflater.inflate(R.layout.fragment_customer_menu, linearLayoutRecentlyAdded, false);

            ImageView foodImageView = foodItemView.findViewById(R.id.foodImageView);
            TextView textViewName = foodItemView.findViewById(R.id.textViewName);
            TextView textViewCategory = foodItemView.findViewById(R.id.textViewCategory);
            TextView textViewPrice = foodItemView.findViewById(R.id.textViewPrice);

            textViewName.setVisibility(View.VISIBLE);

            // Load the image from the Uri
            try {
                InputStream imageStream = getContentResolver().openInputStream(Uri.parse(foodItem.getImageUri()));
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                foodImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            textViewName.setText(foodItem.getName());
            textViewCategory.setText(foodItem.getCategory());
            textViewPrice.setText(String.valueOf(foodItem.getPrice()));

            linearLayoutRecentlyAdded.addView(foodItemView);
        }
    }
}
