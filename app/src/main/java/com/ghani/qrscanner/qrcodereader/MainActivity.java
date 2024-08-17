package com.ghani.qrscanner.qrcodereader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import com.ghani.qrscanner.qrcodereader.Fragment.GenerateFragment;
import com.ghani.qrscanner.qrcodereader.Fragment.HistoryFragment;
import com.ghani.qrscanner.qrcodereader.Fragment.ScanFragment;
import com.ghani.qrscanner.qrcodereader.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ScanFragment scanFragment = new ScanFragment();
        GenerateFragment generateFragment = new GenerateFragment();
        HistoryFragment historyFragment = new HistoryFragment();

        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, scanFragment).commit();

        binding.navBottom.setSelectedItemId(R.id.scan);

        binding.navBottom.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.scan)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, scanFragment)
                        .commit();

                return true;

            } else if (id == R.id.generate) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, generateFragment)
                        .commit();

                return true;

            } else if (id == R.id.history) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, historyFragment)
                        .commit();

                return true;

            } else if (id == R.id.settings) {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

                return true;
            }
            return false;
        });
    }


    @Override
    protected void onResume() {
        applySelectedTheme();
        super.onResume();
    }

    private void applySelectedTheme(){

        String selectedThemeKey = "theme";
        String selectedTheme = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(selectedThemeKey, "system_default");

        if (selectedTheme.equals("dark")) {
            MainActivity.this.setTheme(R.style.Base_Theme_QRBarcodeReader_Dark);
        } else {
            MainActivity.this.setTheme(R.style.Base_Theme_QRBarcodeReader_Light);
        }
    }
}