package com.example.ict603_project_indi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner; // Import the Spinner class
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class calculatorPage extends AppCompatActivity {

    // 1. Declare the UI elements
    EditText editTextKwh;
    EditText editTextRebate;
    Spinner spinnerMonth; // <-- ADDED
    Button buttonCalculate;
    Button buttonHistory;
    Button buttonProfile; // <-- ADDED
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialize the UI elements by finding them in the layout
        editTextKwh = findViewById(R.id.editTextKwh);
        editTextRebate = findViewById(R.id.editTextRebate);
        spinnerMonth = findViewById(R.id.spinnerMonth); // <-- ADDED
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonProfile = findViewById(R.id.buttonProfile); // <-- ADDED
        textViewResult = findViewById(R.id.textViewResult);

        // 3. Set a click listener for the calculate button
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBill();
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calculatorPage.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // Set a click listener for the profile button
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calculatorPage.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    // 4. Method to perform the calculation
    private void calculateBill() {
        // Get the selected month from the spinner
        String selectedMonth = spinnerMonth.getSelectedItem().toString(); // <-- ADDED

        String kwhString = editTextKwh.getText().toString();
        String rebateString = editTextRebate.getText().toString();

        // Check if inputs are empty
        if (kwhString.isEmpty() || rebateString.isEmpty()) {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double kwh = Double.parseDouble(kwhString);
            double rebatePercent = Double.parseDouble(rebateString);

            // Validate rebate percentage
            if (rebatePercent < 0 || rebatePercent > 5) {
                Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalCharges = 0;
            double remainingKwh = kwh;

            // Domestic Tariff Calculation in RM (sen/100)
            // First 200 kWh (1-200) at RM0.218
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 200);
                totalCharges += usage * 0.218;
                remainingKwh -= usage;
            }
            // Next 100 kWh (201-300) at RM0.334
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 100);
                totalCharges += usage * 0.334;
                remainingKwh -= usage;
            }
            // Next 300 kWh (301-600) at RM0.516
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 300);
                totalCharges += usage * 0.516;
                remainingKwh -= usage;
            }
            // Next 300 kWh (601-900) at RM0.546
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 300);
                totalCharges += usage * 0.546;
                remainingKwh -= usage;
            }
            // Over 900 kWh at RM0.571 (This was missing from your spec but is a standard rate)
            if (remainingKwh > 0) {
                totalCharges += remainingKwh * 0.571;
            }

            // Calculate rebate amount
            double rebateAmount = totalCharges * (rebatePercent / 100);

            // Calculate final bill
            double finalBill = totalCharges - rebateAmount;

            // Display the result in the TextView, now including the month and total charges
            // This fulfills the output requirements i and ii
            String resultText = "Month: " + selectedMonth +
                    "\nTotal Charges: RM " + String.format("%.2f", totalCharges) +
                    "\nFinal Cost: RM " + String.format("%.2f", finalBill);
            textViewResult.setText(resultText);

            // Save the calculation to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("CalculationHistory", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(selectedMonth, resultText);
            editor.apply();


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }
}
