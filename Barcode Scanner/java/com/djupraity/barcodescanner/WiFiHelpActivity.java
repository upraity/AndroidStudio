package com.djupraity.barcodescanner;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class WiFiHelpActivity extends AppCompatActivity {
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_help);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // ✅ Now it’s safe to use getSupportActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView textView = findViewById(R.id.helpTextView);
        textView.setText("Wi-Fi Scan Setup Instructions:\n\n" +
                "Follow these steps to scan barcodes and send the data directly to your computer over Wi-Fi:\n\n" +

                "Step 1: Connect to the Same Wi-Fi\n" +
                "Make sure both your phone and computer are connected to the same Wi-Fi network.\n\n" +

                "Step 2: Find Your Computer's IP Address\n" +
                "To find it on Windows:\n" +
                "1. Press Windows + R key together.\n" +
                "2. Type cmd and press Enter. This will open Command Prompt.\n" +
                "3. Type ipconfig and press Enter.\n" +
                "4. Look for the line that says \"IPv4 Address\" — it will look like 192.168.x.x\n" +
                " Note down this IP address.\n\n" +

                "Step 3: Install the Required Software on Your Computer\n" +
                "Enter the link on your pc browser and install the file.(https://djupraity.artizote.com/barcodescanner)\n" +
                "1. Download and install the provided .exe file on your computer.\n" +
                "2. Once installed, run the software. It will start listening for data from your phone.\n" +
                "➡ This allows your PC to automatically receive and paste scanned data.\n\n" +

                "Step 4: Set Desktop IP in the Mobile App\n" +
                "1. Open the Barcode Scanner app on your phone.\n" +
                "2. Tap the three-dot menu (⋮) at the top-right and select \"Set Desktop IP\".\n" +
                "3. Enter the IP address you found in Step 2 and tap Save.\n\n" +

                "Step 5: Start Scanning\n" +
                "1. Go back to the home screen and tap \"Wi-Fi Scan\".\n" +
                "2. Scan any barcode or QR code.\n" +
                "3. The scanned data will be sent directly to your computer automatically.\n\n" +

                "That’s it!\n" +
                "You can now scan and send barcode data from your phone to your PC without typing anything manually.\n\n" +

                "If something doesn't work:\n" +
                "- Make sure both devices are on the same Wi-Fi.\n" +
                "- Ensure the .exe software is running on your computer.\n" +
                "- Check that the IP address is correct.\n");

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Find the AdView and load the ad
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadInterstitialAd();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu); // replace with your menu file name
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Back button pressed
            finish();  // current activity close kar do
            return true;
        } else if (id == R.id.help_menu_item) {
            Toast.makeText(this, "Already in Help", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_set_ip) {
            showIpInputDialog();
            return true;
        }else if (id == R.id.menu_pp) {
            startActivity(new Intent(WiFiHelpActivity.this, PrivacyPolicy.class));
            return true;
        }else if(id == R.id.menu_share_app){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://djupraity.artizote.com/barcodescanner");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }

        return super.onOptionsItemSelected(item);
    }


    private void showIpInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Desktop IP Address");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String savedIp = prefs.getString("desktop_ip", "");
        input.setText(savedIp);

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String ip = input.getText().toString().trim();
            if (isValidIp(ip)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("desktop_ip", ip);
                editor.apply();
                Toast.makeText(WiFiHelpActivity.this, "IP saved: " + ip, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WiFiHelpActivity.this, "Invalid IP address", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private boolean isValidIp(String ip) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";
        return ip.matches(ipPattern);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

//        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, // ✅ Use test ID during development
        InterstitialAd.load(this, "ca-app-pub-6002960092511168/6697652123", adRequest,

                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.d(TAG, "Interstitial Ad Loaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad dismissed.");
                                mInterstitialAd = null;
                                loadInterstitialAd(); // Load another for next use
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d(TAG, "Ad failed to show.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull com.google.android.gms.ads.LoadAdError loadAdError) {
                        mInterstitialAd = null;
                        Log.d(TAG, "Failed to load interstitial ad: " + loadAdError.getMessage());
                    }
                });
    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }

}