package com.testing.foodmanagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {
    private LayoutInflater inflater;
    private List<FoodItem> foodItemList;
    private static final String TAG = "FoodItemAdapter";

    public FoodItemAdapter(Context context, List<FoodItem> foodItems) {
        super(context, 0, foodItems);
        inflater = LayoutInflater.from(context);
        foodItemList = foodItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.food_item_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.foodImageView);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);

        FoodItem foodItem = foodItemList.get(position);

        textViewName.setText(foodItem.getName());
        textViewCategory.setText(foodItem.getCategory());
        textViewPrice.setText(String.valueOf(foodItem.getPrice()));

        try {
            String imageUri = foodItem.getImageUri();
            if (imageUri != null && !imageUri.isEmpty()) {
                int imageResourceId = Integer.parseInt(imageUri);
                imageView.setImageResource(imageResourceId);
            } else {
                // Set a default image or placeholder if the imageUri is null or empty
                imageView.setImageResource(R.drawable.noodles);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid image URI for food item: " + foodItem.getName(), e);
            // Set a default image or placeholder if parsing fails
            imageView.setImageResource(R.drawable.noodles);
        }

        return convertView;
    }
}
