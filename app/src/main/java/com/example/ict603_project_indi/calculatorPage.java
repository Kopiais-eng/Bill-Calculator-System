package com.example.ict603_project_indi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class calculatorPage extends AppCompatActivity {

    // 1. Declare the UI elements
    EditText editTextKwh;
    EditText editTextRebate;
    Spinner spinnerMonth;
    Button buttonCalculate;
    Button buttonHistory;
    Button buttonProfile;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_page);

        // 2. Initialize the UI elements by finding them in the layout
        editTextKwh = findViewById(R.id.editTextKwh);
        editTextRebate = findViewById(R.id.editTextRebate);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonProfile = findViewById(R.id.buttonProfile);
        textViewResult = findViewById(R.id.textViewResult);

        // 3. Set a click listener for the calculate button
        buttonCalculate.setOnClickListener(v -> calculateBill());

        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(calculatorPage.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Set a click listener for the profile button
        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(calculatorPage.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Check for edit data
        Intent intent = getIntent();
        if (intent.hasExtra("historyItem")) {
            String historyItem = intent.getStringExtra("historyItem");
            populateFieldsFromHistory(historyItem);
        }
    }

    private void populateFieldsFromHistory(String historyItem) {
        String month = historyItem.substring(historyItem.indexOf("Month: ") + 7, historyItem.indexOf("\n"));

        // You may need to parse kWh and rebate from the historyItem string
        // For simplicity, we will just set the month here. You can add the parsing logic for kWh and rebate.

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(month);
        spinnerMonth.setSelection(spinnerPosition);
    }

    // 4. Method to perform the calculation
    private void calculateBill() {
        String selectedMonth = spinnerMonth.getSelectedItem().toString();
        String kwhString = editTextKwh.getText().toString();
        String rebateString = editTextRebate.getText().toString();

        if (kwhString.isEmpty() || rebateString.isEmpty()) {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double kwh = Double.parseDouble(kwhString);
            double rebatePercent = Double.parseDouble(rebateString);

            if (rebatePercent < 0 || rebatePercent > 5) {
                Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalCharges = 0;
            double remainingKwh = kwh;

            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 200);
                totalCharges += usage * 0.218;
                remainingKwh -= usage;
            }
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 100);
                totalCharges += usage * 0.334;
                remainingKwh -= usage;
            }
            if (remainingKwh > 0) {
                double usage = Math.min(remainingKwh, 300);
                totalCharges += usage * 0.516;
                remainingKwh -= usage;
            }
            if (remainingKwh > 0) {
                totalCharges += remainingKwh * 0.546;
            }

            double rebateAmount = totalCharges * (rebatePercent / 100);
            double finalBill = totalCharges - rebateAmount;

            String resultText = "Month: " + selectedMonth +
                    "\nTotal Charges: RM " + String.format("%.2f", totalCharges) +
                    "\nFinal Cost: RM " + String.format("%.2f", finalBill);
            textViewResult.setText(resultText);

            SharedPreferences sharedPreferences = getSharedPreferences("CalculationHistory", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(selectedMonth, resultText);
            editor.apply();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }
}
