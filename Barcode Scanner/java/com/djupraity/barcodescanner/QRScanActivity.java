package com.djupraity.barcodescanner;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import android.text.util.Linkify;
import android.text.method.LinkMovementMethod;

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



public class QRScanActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 200;
    private CompoundBarcodeView barcodeView;
    private ImageButton copy, share;
    private TextView resultTextView;
    private boolean canScan = true;
    private MediaPlayer mediaPlayer;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);


        String adUnitId = "ca-app-pub-6002960092511168/5606091991";
        barcodeView = findViewById(R.id.barcode_scanner);
        resultTextView = findViewById(R.id.resultTextView);

        findViewById(R.id.copy).setOnClickListener(v -> {
            String text = resultTextView.getText().toString().trim();
            if (!text.isEmpty()) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Scanned Text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(QRScanActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QRScanActivity.this, "Nothing to copy", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.share).setOnClickListener(v -> {
            String text = resultTextView.getText().toString().trim();
            if (!text.isEmpty()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } else {
                Toast.makeText(QRScanActivity.this, "Nothing to share", Toast.LENGTH_SHORT).show();
            }
        });




        // Initialize MediaPlayer safely
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(this, "Error loading beep sound", Toast.LENGTH_SHORT).show();
        }

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!canScan || result.getText() == null) return;

                String scannedLink = result.getText();
                resultTextView.setText("Scanned: " + scannedLink);
                resultTextView.setAutoLinkMask(0); // Reset any auto linking
                resultTextView.setText(scannedLink);
                Linkify.addLinks(resultTextView, Linkify.WEB_URLS);
                resultTextView.setMovementMethod(LinkMovementMethod.getInstance());
//                resultTextView.setText("Scanned: " + scannedLink);

                // Beep sound
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }

                // Show Toast
                Toast.makeText(QRScanActivity.this, "Link: " + scannedLink, Toast.LENGTH_LONG).show();

                // Open link if valid URL
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedLink));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(QRScanActivity.this, "Invalid link: " + scannedLink, Toast.LENGTH_SHORT).show();
                }

                canScan = false;

                // Optional: Auto restart scanning after 3 seconds
                barcodeView.postDelayed(() -> canScan = true, 3000);
            }
        });

        // Check and request permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startScanning();
        }

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

    private void startScanning() {
        barcodeView.resume();
        canScan = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
        canScan = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission is required to scan.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
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
