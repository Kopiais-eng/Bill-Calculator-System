package com.example.ict603_project_indi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // It's good practice to make member variables private
    private Button newpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Corrected the typo from findviewById to findViewById
        newpage = (Button) findViewById(R.id.button);

        // 2. Set an OnClickListener to handle the button click
        newpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your openActivity method, or move the logic directly here
                openActivity();
            }
        });
    }

    // This method no longer needs the (View view) parameter if called from the listener
    public void openActivity() {
        // Assuming calculatorPage.class is another Activity in your project
        Intent intent = new Intent(MainActivity.this, calculatorPage.class);
        startActivity(intent);
    }
}
