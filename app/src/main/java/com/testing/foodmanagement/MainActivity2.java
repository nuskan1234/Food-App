package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        displayRecentlyAddedItems();
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
