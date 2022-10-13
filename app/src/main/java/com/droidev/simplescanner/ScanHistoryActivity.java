package com.droidev.simplescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ScanHistoryActivity extends AppCompatActivity {

    TextView historyTextView;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        tinyDB = new TinyDB(ScanHistoryActivity.this);

        historyTextView = findViewById(R.id.historyText);

        new Thread(() -> {

            for (int i = 0; i < MainActivity.historyArray.toArray().length; i++) {

                int finalI = i;

                historyTextView.post(() -> {

                    historyTextView.append(MainActivity.historyArray.get(finalI) + "\n\n");

                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuHistory) {

            AlertDialog dialog = new AlertDialog.Builder(ScanHistoryActivity.this)
                    .setCancelable(false)
                    .setTitle("Delete scan history?")
                    .setPositiveButton("Yes", null)
                    .setNegativeButton("No", null)
                    .show();

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(v -> {

                historyTextView.setText("");

                tinyDB.remove("history");

                MainActivity.historyArray.clear();

                dialog.dismiss();
            });

            negativeButton.setOnClickListener(v -> dialog.dismiss());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}