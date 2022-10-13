package com.droidev.simplescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Utils utils;
    TinyDB tinyDB;
    public static ArrayList<String> historyArray;
    public static Boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(MainActivity.this);

        historyArray = new ArrayList<>();

        historyArray.addAll(tinyDB.getListString("history"));

        utils = new Utils();

        utils.scanner(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (opened) {

            opened = false;

            utils.scanner(MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                finish();
            } else {

                if (URLUtil.isValidUrl(intentResult.getContents())) {

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("Website link detected")
                            .setMessage("Open ''" + intentResult.getContents() + "'' in your Web Browser?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("No", null)
                            .setNeutralButton("Show text", null)
                            .show();

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                    positiveButton.setOnClickListener(v -> {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentResult.getContents()));
                        startActivity(browserIntent);

                        opened = true;

                        dialog.dismiss();
                    });

                    negativeButton.setOnClickListener(v -> {

                        utils.scanner(MainActivity.this);

                        dialog.dismiss();
                    });

                    neutralButton.setOnClickListener(v -> {

                        Intent i = new Intent(this, ScanResultActivity.class);
                        i.putExtra("text", intentResult.getContents());
                        startActivity(i);

                        dialog.dismiss();
                    });
                } else {

                    Intent i = new Intent(this, ScanResultActivity.class);
                    i.putExtra("text", intentResult.getContents());
                    startActivity(i);
                }

                saveHistory(intentResult.getContents());
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveHistory(String s) {

        historyArray.add(s);

        tinyDB.remove("history");

        tinyDB.putListString("history", historyArray);
    }

}