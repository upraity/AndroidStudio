package com.djupraity.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

public class MainActivity extends AppCompatActivity {

    Button qrButton, barcodeButton, generateqr;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "Admob";
    String adUnitId = "ca-app-pub-6002960092511168/5606091991";
    private RewardedAd rewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        qrButton = findViewById(R.id.qrButton);
        barcodeButton = findViewById(R.id.barcodeButton);
        generateqr = findViewById(R.id.generateqr);

        qrButton.setOnClickListener(v -> {
            showInterstitialAd(); // Show ad before navigating
            Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
            startActivity(intent);
        });

        barcodeButton.setOnClickListener(v -> {
            showInterstitialAd(); // Show ad before navigating
            Intent intent = new Intent(MainActivity.this, ScannerTypeActivity.class);
            startActivity(intent);
        });

        generateqr.setOnClickListener(v -> {
            showInterstitialAd(); // Show ad before navigating
            Intent intent = new Intent(MainActivity.this, QRGeneratorActivity.class);
            startActivity(intent);
        });

        // Initialize AdMob
        MobileAds.initialize(this, initializationStatus -> {});

        // Load banner ad
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Load interstitial ad
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
