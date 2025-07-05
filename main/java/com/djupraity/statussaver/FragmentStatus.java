package com.djupraity.statussaver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.djupraity.statussaver.databinding.FragmentStatusBinding;

public class FragmentStatus extends Fragment {

    private FragmentStatusBinding binding;

    public static FragmentStatus newInstance(String type) {
        FragmentStatus fragment = new FragmentStatus();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String type = getArguments().getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

////package com.djupraity.statussaver;
////
////import androidx.fragment.app.Fragment;
////
////public class FragmentStatus {
////    public static Fragment newInstance(String whatsappMain) {
////    }
////}
//
//package com.djupraity.statussaver;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.djupraity.statussaver.databinding.FragmentStatusBinding;
//
//public class FragmentStatus extends Fragment {
//
//    private FragmentStatusBinding binding;
//
//    public static FragmentStatus newInstance(String type) {
//        FragmentStatus fragment = new FragmentStatus();
//        Bundle args = new Bundle();
//        args.putString("type", type);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Optional: Retrieve arguments
//        if (getArguments() != null) {
//            String type = getArguments().getString("type");
//            // Use the type argument as needed
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout using view binding
//        binding = FragmentStatusBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null; // Avoid memory leaks
//    }
//}
