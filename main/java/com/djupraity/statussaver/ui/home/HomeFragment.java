//package com.djupraity.statussaver.ui.home;
//
//import android.Manifest;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.djupraity.statussaver.databinding.FragmentHomeBinding;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.MultiplePermissionsReport;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}

//
//package com.djupraity.statussaver.ui.home;
//
//import android.Manifest;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.djupraity.statussaver.R;
//import com.djupraity.statussaver.databinding.FragmentHomeBinding;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.MultiplePermissionsReport;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Check permissions and update layout accordingly
//        checkPermissions(inflater, container);
//
//        return binding.getRoot();
//    }
//
//    private void checkPermissions(LayoutInflater inflater, ViewGroup container) {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            // Show the main content if permissions are granted
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//
//                            // Inflate and display the layout_permission.xml if permissions are denied
//                            View permissionView = inflater.inflate(R.layout.layout_permission, container, false);
//
//                            // Add the permissionView to the fragment's root
//                            binding.getRoot().removeAllViews(); // Clear previous content
//                            binding.getRoot().addView(permissionView);
//
//                            // Handle the Grant Permissions button
//                            permissionView.findViewById(R.id.btn_permission).setOnClickListener(v -> checkPermissions(inflater, container));
//
//                        } else {
//
//                            // Inflate and display the layout_permission.xml if permissions are denied
//                            View permissionView = inflater.inflate(R.layout.layout_permission, container, false);
//
//                            // Add the permissionView to the fragment's root
//                            binding.getRoot().removeAllViews(); // Clear previous content
//                            binding.getRoot().addView(permissionView);
//
//                            // Handle the Grant Permissions button
//                            permissionView.findViewById(R.id.btn_permission).setOnClickListener(v -> checkPermissions(inflater, container));
//
//
//                           }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}

package com.djupraity.statussaver.ui.home;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.djupraity.statussaver.R;
import com.djupraity.statussaver.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupTabLayoutAndViewPager();
        checkPermissions();

        binding.btnPermission.setOnClickListener(v -> checkPermissions());

        return binding.getRoot();
    }

    private void setupTabLayoutAndViewPager() {
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Images");
            } else {
                tab.setText("Videos");
            }
        }).attach();
    }

    private void checkPermissions() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (binding == null) return;

                        if (report.areAllPermissionsGranted()) {
                            binding.contentLayout.setVisibility(View.VISIBLE);
                            binding.permissionLayout.setVisibility(View.GONE);
                        } else {
                            binding.contentLayout.setVisibility(View.GONE);
                            binding.permissionLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout for this fragment and bind views
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Initially check permissions
//        checkPermissions();
//
//        return binding.getRoot();
//    }
//
//    private void checkPermissions() {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() { 
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            // If permissions are granted, show the main content and hide permission layout
//                            if (binding != null) {
//                                binding.contentLayout.setVisibility(View.VISIBLE);  // Show content layout
//                                binding.appBarLayout.setVisibility(View.GONE);  // Hide permission request layout
//                            }
//                        } else {
//                            // If permissions are denied, show the permission layout and hide content layout
//                            if (binding != null) {
//                                binding.contentLayout.setVisibility(View.GONE);  // Hide content layout
//                                binding.appBarLayout.setVisibility(View.VISIBLE);  // Show permission layout
//                            }
//
//                            // Handle the Grant Permissions button click to request permissions again
//                            binding.appBarLayout.findViewById(R.id.btn_permission).setOnClickListener(v -> checkPermissions());
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();  // Continue the permission request flow
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;  // Clean up binding when view is destroyed to avoid memory leaks
//    }
//}
//

//it work but not properly here layout permission not see
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Check permissions and handle accordingly
//        checkPermissions();
//
//        return binding.getRoot();
//    }
//
//    private void checkPermissions() {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (binding == null) return;
//
//                        // If permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            // Show content and hide permission layout
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//                            binding.appBarLayout.setVisibility(View.GONE);
//                        } else {
//                            // If permissions are denied, show permission layout
//                            binding.contentLayout.setVisibility(View.GONE);
//                            binding.appBarLayout.setVisibility(View.VISIBLE);
//
//                            // Handle the button click to request permissions again
//                            binding.contentLayout.setOnClickListener(v -> checkPermissions());
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();  // Continue requesting permissions
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;  // Clean up binding when the view is destroyed
//    }
//}
//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Check permissions and handle accordingly
//        checkPermissions(inflater, container);
//
//        return binding.getRoot();
//    }
//
//    private void checkPermissions(LayoutInflater inflater, ViewGroup container) {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (binding == null) return;
//
//                        // If permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            // Show content and hide permission layout
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//                            binding.contentLayout.setVisibility(View.GONE);
//                        } else {
//                            // If permissions are denied, show permission layout
//                            binding.contentLayout.setVisibility(View.GONE);
//
//                            // Inflate the layout_permission.xml view if permissions are denied
//                            View permissionView = inflater.inflate(R.layout.layout_permission, container, false);
//
//                            // Remove previous views and add the permission view
//                            binding.getRoot().removeAllViews();  // Clear previous content
//                            binding.getRoot().addView(permissionView);  // Add permission view
//
//                            // Handle the Grant Permissions button click to request permissions again
//                            permissionView.findViewById(R.id.btn_permission).setOnClickListener(v -> checkPermissions(inflater, container));
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();  // Continue requesting permissions
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;  // Clean up binding when the view is destroyed
//    }
//}

//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Check permissions and handle accordingly
//        showPermissionLayout(inflater, container);  // Initial call to show permission layout
//
//        return binding.getRoot();
//    }
//
//    private void showPermissionLayout(LayoutInflater inflater, ViewGroup container) {
//        // Inflate layout_permission.xml to show permission request UI
//        View permissionView = inflater.inflate(R.layout.layout_permission, container, false);
//
//        // Clear any previous content and add the permission layout
//        binding.getRoot().removeAllViews();  // Clear previous views
//        binding.getRoot().addView(permissionView);  // Add permission view
//
//        // Handle button click to request permissions when denied
//        permissionView.findViewById(R.id.btn_permission).setOnClickListener(v -> checkPermissions(inflater, container));
//    }
//
//    private void checkPermissions(LayoutInflater inflater, ViewGroup container) {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (binding == null) return;
//
//                        // If permissions are granted, show the main content
//                        if (report.areAllPermissionsGranted()) {
//                            // Show main content and hide permission layout
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//                            binding.contentLayout.setVisibility(View.GONE);
//                        } else {
//                            // If permissions are denied, show the permission layout again
//                            binding.contentLayout.setVisibility(View.GONE);
//
//                            // Reinflate the permission layout to request permissions again
//                            showPermissionLayout(inflater, container);
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();  // Continue permission request
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;  // Clean up binding when the view is destroyed
//    }
//}

//package com.djupraity.statussaver.ui.home;
//
//import android.Manifest;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.djupraity.statussaver.R;
//import com.djupraity.statussaver.databinding.FragmentHomeBinding;
//import com.djupraity.statussaver.databinding.LayoutPermissionBinding;
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.MultiplePermissionsReport;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private LayoutPermissionBinding permissionBinding;
//    private FragmentHomeBinding mainBinding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout_permission.xml by default
//        permissionBinding = LayoutPermissionBinding.inflate(inflater, container, false);
//
//        // Set up the button click listener for the permission button
//        permissionBinding.btnPermission.setOnClickListener(v -> requestPermissions());
//
//        return permissionBinding.getRoot(); // Show the layout_permission.xml initially
//    }
//
//    private void requestPermissions() {
//        // Request permissions using Dexter
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            // Show the main content if permissions are granted
//                            loadMainContent();
//                        } else {
//                            // Permissions are denied, show the permission request UI again
//                            showPermissionRequestUI();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        // Continue permission request flow
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }
//
//    private void showPermissionRequestUI() {
//        // Inflate and display the layout_permission.xml if permissions are denied
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup container = (ViewGroup) permissionBinding.getRoot().getParent();
//
//        // Inflate the permission layout
//        View permissionView = inflater.inflate(R.layout.layout_permission, container, false);
//
//        // Clear previous content and add permission request view
//        if (container != null) {
//            container.removeAllViews(); // Remove previous content
//            container.addView(permissionView); // Add permission layout
//        }
//
//        // Handle the Grant Permissions button
//        permissionView.findViewById(R.id.btn_permission).setOnClickListener(v -> requestPermissions());
//    }
//
//    private void loadMainContent() {
//        // Create a new instance of the Fragment to replace the current one
//        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
//
//        // Inflate the main content layout fragment (FragmentHomeBinding)
//        mainBinding = FragmentHomeBinding.inflate(getLayoutInflater());
//
//        // Replace the fragment with the new one
//        transaction.replace(R.id.container, new Fragment());  // Use your Fragment container's ID
//        transaction.commit();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // Clean up bindings to prevent memory leaks
//        permissionBinding = null;
//        mainBinding = null;
//    }
//}
//
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Check permissions and handle accordingly
//        checkPermissions();
//
//        return binding.getRoot();
//    }
//
//    private void checkPermissions() {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (binding == null) return;
//
//                        // If permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            // Show the main content layout and hide the permission layout
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//                            binding.permissionLayout.setVisibility(View.GONE);
//                        } else {
//                            // If permissions are denied, show permission layout
//                            binding.contentLayout.setVisibility(View.GONE);
//                            binding.permissionLayout.setVisibility(View.VISIBLE);
//
//                            // Handle the button click to request permissions again
//                            binding.btnPermission.setOnClickListener(v -> checkPermissions());
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();  // Continue requesting permissions
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;  // Clean up binding when the view is destroyed
//    }
//}
//public class HomeFragment extends Fragment {
//
//    private FragmentHomeBinding binding;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the view using ViewBinding
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//
//        // Set up the TabLayout and ViewPager2
//        setupTabLayoutAndViewPager();
//
//        // Check permissions and set up UI accordingly
//        checkPermissions();
//
//        // Set click listener for the permission button
//        binding.btnPermission.setOnClickListener(v -> checkPermissions());
//
//        return binding.getRoot();
//    }
//
//    private void setupTabLayoutAndViewPager() {
//        TabLayout tabLayout = binding.tabLayout;
//        ViewPager2 viewPager = binding.viewPager;
//
//        // Set up the ViewPager2 with the FragmentStateAdapter
//        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(requireActivity());
//        viewPager.setAdapter(adapter);
//
//        // Attach the TabLayout to the ViewPager2
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            if (position == 0) {
//                tab.setText("Images");
//            } else {
//                tab.setText("Videos");
//            }
//        }).attach();
//    }
//
//    private void checkPermissions() {
//        Dexter.withContext(getContext())
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (binding == null) return;
//
//                        if (report.areAllPermissionsGranted()) {
//                            // Permissions granted: Show content layout and hide permission layout
//                            binding.contentLayout.setVisibility(View.VISIBLE);
//                            binding.permissionLayout.setVisibility(View.GONE);
//                        } else {
//                            // Permissions denied: Show permission layout and hide content layout
//                            binding.contentLayout.setVisibility(View.GONE);
//                            binding.permissionLayout.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        // Show rationale for permissions and continue requesting
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // Clean up binding when the view is destroyed
//        binding = null;
//    }
//}
//
