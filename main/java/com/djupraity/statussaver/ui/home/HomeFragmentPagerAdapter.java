package com.djupraity.statussaver.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.djupraity.statussaver.ImageFragment;
import com.djupraity.statussaver.VideoFragment;

public class HomeFragmentPagerAdapter extends FragmentStateAdapter {

    public HomeFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ImageFragment(); // Your existing fragment for images
        } else {
            return new VideoFragment(); // Your existing fragment for videos
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

