package com.testing.foodmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

public class AddBranchActivity extends AppCompatActivity {

    private EditText branchNameEditText;
    private EditText branchPhoneEditText;
    private EditText branchEmailEditText;
    private EditText branchOpenHoursEditText;
    private Button selectLocationButton;
    private Button registerBranchButton;

    private LatLng selectedLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        branchNameEditText = findViewById(R.id.branchNameEditText);
        branchPhoneEditText = findViewById(R.id.branchPhoneEditText);
        branchEmailEditText = findViewById(R.id.branchEmailEditText);
        branchOpenHoursEditText = findViewById(R.id.branchOpenHoursEditText);
        selectLocationButton = findViewById(R.id.selectLocationButton);
        registerBranchButton = findViewById(R.id.registerBranchButton);

        selectLocationButton.setOnClickListener(v -> {
            // Code to launch Google Maps activity to select location
            Intent intent = new Intent(AddBranchActivity.this, SelectLocationActivity.class);
            startActivityForResult(intent, 1);
        });

        registerBranchButton.setOnClickListener(v -> {
            // Code to register branch with the details
            String branchName = branchNameEditText.getText().toString();
            String phone = branchPhoneEditText.getText().toString();
            String email = branchEmailEditText.getText().toString();
            String openHours = branchOpenHoursEditText.getText().toString();

            if (selectedLocation != null) {
                String location = selectedLocation.latitude + "," + selectedLocation.longitude;

                // Save branch information to database
                DBHelper dbHelper = new DBHelper(this);
                dbHelper.addBranch(new Branch(branchName, phone, email, openHours, location));
            } else {
                // Handle the case where location is not selected
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Handle location result from Google Maps
            selectedLocation = data.getParcelableExtra("location");
        }
    }
}
