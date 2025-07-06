package com.djupraity.barcodescanner;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

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


import java.io.OutputStream;
import java.net.Socket;

public class WiFiScanActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 200;
    private CompoundBarcodeView barcodeView;
    private TextView resultTextView;
    private ImageButton copy, share;
    private String scannedData = "";
    private boolean canScan = true;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    private MediaPlayer mediaPlayer; // For beep sound

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        barcodeView = findViewById(R.id.barcode_scanner);
        resultTextView = findViewById(R.id.resultTextView);

        findViewById(R.id.copy).setOnClickListener(v -> {
            String text = resultTextView.getText().toString().trim();
            if (!text.isEmpty()) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Scanned Text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(WiFiScanActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WiFiScanActivity.this, "Nothing to copy", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(WiFiScanActivity.this, "Nothing to share", Toast.LENGTH_SHORT).show();
            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.beep); // Sound file from res/raw/beep.mp3

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String savedIp = prefs.getString("desktop_ip", "");

        if (savedIp.isEmpty()) {
            showIpInputDialog();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
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
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!canScan || result.getText() == null) return;

                scannedData = result.getText();
                resultTextView.setText("Scanned: " + scannedData);

                // Play beep sound
                if (mediaPlayer != null) mediaPlayer.start();

                // Send scanned data to desktop
                sendDataOverWiFi(scannedData);

                // Stop further scanning until user responds
                canScan = false;
                runOnUiThread(() -> showScanAgainDialog());
            }
        });
    }

    private void sendDataOverWiFi(String data) {
        new Thread(() -> {
            try {
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                String ipAddress = prefs.getString("desktop_ip", "");
                if (ipAddress.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(WiFiScanActivity.this,
                            "Desktop IP not set! Please set it in the app.", Toast.LENGTH_LONG).show());
                    return;
                }

                Socket socket = new Socket(ipAddress, 5000);
                OutputStream output = socket.getOutputStream();
                output.write(data.getBytes());
                output.flush();
                socket.close();
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(WiFiScanActivity.this,
                        "Error sending data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void showScanAgainDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Scan Completed")
                .setMessage("Do you want to scan again?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    canScan = true;
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    canScan = false;
                    dialog.dismiss();
                })
                .show();
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
                Toast.makeText(WiFiScanActivity.this, "IP saved: " + ip, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WiFiScanActivity.this, "Invalid IP address", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private boolean isValidIp(String ip) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";
        return ip.matches(ipPattern);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help_menu_item) {
            startActivity(new Intent(WiFiScanActivity.this, WiFiHelpActivity.class));
            return true;
        } else if (id == R.id.menu_set_ip) {
            showIpInputDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
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
