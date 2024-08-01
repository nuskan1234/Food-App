package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Find the CardView for adding a menu
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView cardViewMenu;
        cardViewMenu = findViewById(R.id.card_view_menu);

        // Set a click listener on the CardView
        cardViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddMenuActivity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewMenuActivity.class);
                startActivity(intent);
            }
        });
        CardView cardAddMenu = findViewById(R.id.card_add_menu);

        cardAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
