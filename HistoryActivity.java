package com.example.ict603_project_indi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView textViewHistory = findViewById(R.id.textViewHistory);

        SharedPreferences sharedPreferences = getSharedPreferences("CalculationHistory", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // Use a StringBuilder to efficiently build the string
        StringBuilder historyBuilder = new StringBuilder();

        // Check if there's any history
        if (allEntries.isEmpty()) {
            historyBuilder.append("No history found.");
        } else {
            // Loop through all saved calculations and append them to the StringBuilder
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                historyBuilder.append(entry.getValue().toString()).append("\n\n");
            }
        }

        // Set the final text to the TextView
        textViewHistory.setText(historyBuilder.toString());
    }
}
