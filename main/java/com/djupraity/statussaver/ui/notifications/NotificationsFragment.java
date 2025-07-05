package com.djupraity.statussaver.ui.notifications;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.djupraity.statussaver.R;
import com.djupraity.statussaver.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root.findViewById(R.id.theme).setOnClickListener(v -> showThemeDialog(AppCompatDelegate.MODE_NIGHT_NO));
        root.findViewById(R.id.sharebtn).setOnClickListener(v -> shareContent());
        root.findViewById(R.id.contactus).setOnClickListener(v -> {
            // Telegram chat URL
            String telegramChatUrl = "https://t.me/sabkacode"; // Replace 'your_chat_username' with your Telegram username or chat link

            // Intent to open the URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegramChatUrl));

            // Check if any app can handle the intent
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "No app found to open this link", Toast.LENGTH_SHORT).show();
            }
        });

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void shareContent() {
        String shareText = "https://djupraity.artizote.com/StatusSaver.apk"; // Text to share
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Show the chooser dialog
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void showThemeDialog(int modeNightNo) {
        String[] themes = {"Light", "Dark"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Theme")
                .setItems(themes, (dialog, which) -> {
                    if (which == 0) {
                        setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                    } else if (which == 1) {
                        setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                })
                .show();
    }

    private void setTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);

        SharedPreferences preferences = requireActivity().getSharedPreferences("AppTheme", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();

        requireActivity().recreate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}