package com.testing.foodmanagement;

public class FoodItem {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private String ingredients;
    private boolean available; // Changed to boolean
    private String imageUri;

    public FoodItem(int id, String name, String category, String description, double price, String ingredients, boolean available, String imageResId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.available = available; // No need to convert to int here
        this.imageUri = imageUri;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getIngredients() { return ingredients; }
    public boolean isAvailable() { return available; } // Returns boolean
    public String getImageUri() { return imageUri; }
}
