package com.djupraity.statussaver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.Manifest;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.djupraity.statussaver.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTheme();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        checkPermission();
    }

//    private void checkPermission(){
//        Dexter.withContext(this)
//                .withPermissions(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                ).withListener(new MultiplePermissionsListener() {
//                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if(!report.areAllPermissionsGranted()){
//                            checkPermission();
//                        }
//                    }
//                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
//                }).check();
//    }

    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
        int mode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

}

//package com.djupraity.statussaver;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.djupraity.statussaver.databinding.ActivityMainBinding;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        loadTheme();
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//
//        // Set up the BottomNavigationView listener
//        navView.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
////            switch (item.getItemId()) {
////                case R.id.navigation_home:
////                    selectedFragment = FragmentStatus.newInstance("WHATSAPP_MAIN");
////                    break;
////
////                case R.id.navigation_dashboard:
////                    selectedFragment = FragmentStatus.newInstance("WHATSAPP_BUSINESS");
////                    break;
////
////                case R.id.navigation_notifications:
////                    selectedFragment = new FragmentSettings();
////                    break;
////            }
////
////            if (selectedFragment != null) {
////                replaceFragment(selectedFragment);
////            }
//            int itemId = item.getItemId();
//            if (itemId == R.id.navigation_home) {
//                selectedFragment = FragmentStatus.newInstance("WHATSAPP_MAIN");
//
//                // Handle Home Navigation
//            } else if (itemId == R.id.navigation_dashboard) {
//                selectedFragment = FragmentStatus.newInstance("WHATSAPP_BUSINESS");
//                // Handle Dashboard Navigation
//            } else if (itemId == R.id.navigation_notifications) {
//                // Handle Notifications Navigation
//            }
////            return true;
//            return true;
//        });
//
//        // Load the default fragment
//        if (savedInstanceState == null) {
//            replaceFragment(FragmentStatus.newInstance("WHATSAPP_MAIN"));
//        }
//    }
//
//    private void replaceFragment(Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment_activity_main, fragment)
//                .commit();
//    }
//
//    private void loadTheme() {
//        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
//        int mode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
//        AppCompatDelegate.setDefaultNightMode(mode);
//    }
//}
