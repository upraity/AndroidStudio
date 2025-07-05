package com.djupraity.statussaver;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djupraity.statussaver.adapter.WhatsAppStatusAdapter;
import com.djupraity.statussaver.databinding.FragmentImageBinding;
import com.djupraity.statussaver.model.WhatsAppStatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {

    private FragmentImageBinding binding;
    private ArrayList<WhatsAppStatusModel> list;
    private WhatsAppStatusAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        list = new ArrayList<>();
        setupRecyclerView();
        getData();

        binding.refresh.setOnRefreshListener(() -> {
            list.clear();
            getData();
            binding.refresh.setRefreshing(false);
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new WhatsAppStatusAdapter(getContext(), list);
        binding.mediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mediaRecyclerView.setAdapter(adapter);
    }

    private void getData() {
        list.clear();
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.statuses";
        File targetDirectory = new File(targetPath);

        if (targetDirectory.exists() && targetDirectory.isDirectory()) {
            File[] allFiles = targetDirectory.listFiles();

            if (allFiles != null && allFiles.length > 0) {
                Arrays.sort(allFiles, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));

                for (File file : allFiles) {
                    if (file.isFile() && isVideoFile(file)) {
                        list.add(new WhatsAppStatusModel(
                                "video",
                                Uri.fromFile(file),
                                file.getAbsolutePath(),
                                file.getName()
                        ));
                    }
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private boolean isVideoFile(File file) {
        String filePath = file.getAbsolutePath().toLowerCase();
        return filePath.endsWith(".mp4") || filePath.endsWith(".mkv") || filePath.endsWith(".avi");
    }
}


//package com.djupraity.statussaver;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.djupraity.statussaver.adapter.WhatsAppStatusAdapter;
//import com.djupraity.statussaver.databinding.FragmentImageBinding;
//import com.djupraity.statussaver.model.WhatsAppStatusModel;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class VideoFragment extends Fragment {
//
//    private FragmentImageBinding binding;
//    private ArrayList<WhatsAppStatusModel> list; // Corrected the type
//    private WhatsAppStatusAdapter adapter;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);
//
//        list = new ArrayList<>();
//        setupRecyclerView();
//        getData();
//
//        // Refresh functionality
//        binding.refresh.setOnRefreshListener(() -> {
//            list.clear();
//            getData();
//            binding.refresh.setRefreshing(false);
//        });
//
//        return binding.getRoot();
//    }
//
//    /**
//     * Sets up the RecyclerView with a LinearLayoutManager and initializes the adapter.
//     */
//    private void setupRecyclerView() {
//        adapter = new WhatsAppStatusAdapter(getContext(), list);
//        binding.mediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.mediaRecyclerView.setAdapter(adapter);
//    }
//
//    /**
//     * Fetches video statuses from the WhatsApp .Statuses folder.
//     */
//    private void getData() {
//        list.clear(); // Clear the list to avoid duplicates
//        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
//        File targetDirectory = new File(targetPath);
//
//        if (targetDirectory.exists()) {
//            File[] allFiles = targetDirectory.listFiles();
//
//            if (allFiles != null && allFiles.length > 0) {
//                // Sort files by last modified in descending order
//                Arrays.sort(allFiles, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
//
//                for (File file : allFiles) {
//                    if (file.isFile() && isVideoFile(file)) {
////                        list.add(new WhatsAppStatusModel(
////                                "Video Status",
//////                                Uri.fromFile(file),
//////                                file.getAbsolutePath(),
////                                file.getName()
////                        ));
//                    }
//                }
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//    }
//
//    /**
//     * Checks if the given file is a video (e.g., .mp4).
//     *
//     * @param file The file to check.
//     * @return True if the file is a video, false otherwise.
//     */
//    private boolean isVideoFile(File file) {
//        String filePath = file.getAbsolutePath().toLowerCase();
//        return filePath.endsWith(".mp4");
//    }
//}
