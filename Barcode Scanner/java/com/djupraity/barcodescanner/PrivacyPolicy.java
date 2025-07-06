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
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;


public class PrivacyPolicy extends AppCompatActivity {
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    String adUnitId = "ca-app-pub-6002960092511168/5606091991";
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacypoilicy);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // ✅ Now it’s safe to use getSupportActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView textView = findViewById(R.id.helpTextView);
        textView.setText("📜 Privacy Policy\n\n" +
                "This app requires only camera permission in order to scan QR codes and barcodes.\n\n" +
                "🔒 We do NOT collect, store, or share any personal data from users.\n\n" +
                "📢 Advertisements:\n" +
                "This app displays ads which may be based on general usage data by Google AdMob.\n\n" +
                "💻 Data to Laptop:\n" +
                "The scanned barcode or QR code data can be transferred to a laptop over Wi-Fi.\n" +
                "To do this:\n" +
                "• You need to install a companion tool on your computer.\n" +
                "• Enter your Laptop's IP Address in the app to connect.\n\n" +
                "This process happens locally over your own Wi-Fi network. No data is sent to any server.\n\n" +
                "✅ By using this app, you agree to this privacy policy.");

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Find the AdView and load the ad
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadInterstitialAd();
        loadRewardedAd();
    }
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", // <-- Test ad unit ID
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("Ad", "Rewarded ad loaded.");
                    }

//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
//                        Log.d("Ad", "Failed to load rewarded ad: " + adError.getMessage());
//                        rewardedAd = null;
//                    }
                });
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
            startActivity(new Intent(PrivacyPolicy.this, WiFiHelpActivity.class));
            return true;
        } else if (id == R.id.menu_set_ip) {
            showIpInputDialog();
            return true;
        }else if (id == R.id.menu_pp) {
            // Current activity hi PrivacyPolicy hai, toh kuch na karo ya toast dikhado
            Toast.makeText(this, "Already in Privacy Policy", Toast.LENGTH_SHORT).show();
            return true;
        } else if(id == R.id.menu_share_app){
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
                Toast.makeText(PrivacyPolicy.this, "IP saved: " + ip, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PrivacyPolicy.this, "Invalid IP address", Toast.LENGTH_SHORT).show();
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
