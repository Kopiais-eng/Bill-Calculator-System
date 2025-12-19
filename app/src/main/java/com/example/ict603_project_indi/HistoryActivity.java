package com.example.ict603_project_indi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnItemClickListener {

    private RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;
    private List<String> historyList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("CalculationHistory", MODE_PRIVATE);
        loadHistory();
    }

    private void loadHistory() {
        historyList = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        if (!allEntries.isEmpty()) {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                historyList.add(entry.getValue().toString());
            }
        }

        historyAdapter = new HistoryAdapter(historyList, this);
        recyclerViewHistory.setAdapter(historyAdapter);
    }

    @Override
    public void onEditClick(int position) {
        String historyItem = historyList.get(position);
        Intent intent = new Intent(this, calculatorPage.class);
        intent.putExtra("historyItem", historyItem);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        String historyItem = historyList.get(position);
        String month = historyItem.substring(historyItem.indexOf("Month: ") + 7, historyItem.indexOf("\n"));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(month);
        editor.apply();

        historyList.remove(position);
        historyAdapter.notifyItemRemoved(position);
    }
}
