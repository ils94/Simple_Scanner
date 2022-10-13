package com.droidev.simplescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        setTitle("Scan Result");

        text = findViewById(R.id.textReceived);

        Intent intent = getIntent();
        String textReceived = intent.getExtras().getString("text");

        text.setText(textReceived);

        MainActivity.opened = true;
    }
}