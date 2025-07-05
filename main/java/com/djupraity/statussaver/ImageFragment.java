package com.djupraity.statussaver;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;
    private ArrayList<WhatsAppStatusModel> list;
    private WhatsAppStatusAdapter adapter;
    private static final String TAG = "ImageFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        list = new ArrayList<>();
        setupRecyclerView();
        fetchData();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new WhatsAppStatusAdapter(getContext(), list);
        binding.mediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mediaRecyclerView.setAdapter(adapter);
    }

    private void fetchData() {
        list.clear();
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.statuses";
        File targetDirectory = new File(targetPath);

        if (targetDirectory.exists() && targetDirectory.isDirectory()) {
            File[] allFiles = targetDirectory.listFiles();

            if (allFiles != null && allFiles.length > 0) {
                Arrays.sort(allFiles, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));

                for (File file : allFiles) {
                    if (file.isFile() && isImageFile(file)) {
                        list.add(new WhatsAppStatusModel(
                                "image",
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

    private boolean isImageFile(File file) {
        String filePath = file.getAbsolutePath().toLowerCase();
        return filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png");
    }
}


//package com.djupraity.statussaver;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
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
//public class ImageFragment extends Fragment {
//
//    private FragmentImageBinding binding;
//    private ArrayList<WhatsAppStatusModel> list;
//    private WhatsAppStatusAdapter adapter;
//    private static final String TAG = "ImageFragment";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);
//
//        list = new ArrayList<>();
//        setupRecyclerView();
//        fetchData();
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
//     * Fetches WhatsApp image statuses from the .statuses folder.
//     */
//    private void fetchData() {
//        list.clear(); // Clear the list to avoid duplicates
//        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.statuses";
//        File targetDirectory = new File(targetPath);
//
//        if (targetDirectory.exists() && targetDirectory.isDirectory()) {
//            File[] allFiles = targetDirectory.listFiles();
//
//            if (allFiles != null && allFiles.length > 0) {
//                // Sort files by last modified in descending order
//                Arrays.sort(allFiles, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
//
//                for (File file : allFiles) {
//                    if (file.isFile() && isImageFile(file)) {
//                        list.add(new WhatsAppStatusModel(
//                                "image",                      // Type of status
//                                Uri.fromFile(file), // File URI as a String
//                                file.getAbsolutePath(),        // Absolute path of the file
//                                file.getName()                 // File name
//                        ));
//                    }
//                }
//            } else {
//                Log.w(TAG, "No files found in directory: " + targetPath);
//            }
//        } else {
//            Log.e(TAG, "Target directory does not exist or is not a directory: " + targetPath);
//        }
//
//        // Notify adapter about data changes
//        adapter.notifyDataSetChanged();
//    }
//
//    /**
//     * Checks if the given file is an image (e.g., .jpg, .png).
//     *
//     * @param file The file to check.
//     * @return True if the file is an image, false otherwise.
//     */
//    private boolean isImageFile(File file) {
//        String filePath = file.getAbsolutePath().toLowerCase();
//        return filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png");
//    }
//}




//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.databinding.ViewDataBinding;
//import androidx.fragment.app.Fragment;
//
//import com.djupraity.statussaver.adapter.WhatsAppStatusAdapter;
//import com.djupraity.statussaver.adapter.WhatsappStatusAdapter;
//import com.djupraity.statussaver.model.WhatsAppStatusModel;
//import com.djupraity.statussaver.model.WhatsappStatusModel;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class ImageFragment extends Fragment {
//    private ViewDataBinding binding;
//    private ArrayList<WhatsAppStatusModel> list;
//    private WhatsAppStatusAdapter adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState){
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image,
//                container, false);
//
//        list = new ArrayList<>();
//        getData();
//
//        binding..setOnKeyListener(() -> {
//            list = new ArrayList<>();
//            getData();
//            binding..setPadding(false);
//        });
//        return binding.getRoot();
//    }
//
//    private void getData() {
//        WhatsappStatusModel model;
//        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                "WhatsApp/Media/.statuses";
//        File targetDirector = new File(targetPath);
//        File[] allFiles = targetDirector.listFiles();
//
//        Arrays.sort(allFiles, (((o1, o2) -> {
//            if(o1.lastModified() > o2.lastModified()) return -1;
//            else if (o1.lastModified() < o2.lastModified()) return +1;
//            else return 0;
//        })));
//
//        for (int i=0; i< allFiles.length; i++){
//            File file = allFiles[i];
//            if(Uri.fromFile(file).toString().endsWith(".png") ||
//                    Uri.fromFile(file).toString().endsWith(".jpg")){
//                model = new WhatsappStatusModel("whats "+i,
//                        Uri.fromFile(file),
//                        allFiles[i].getAbsolutePath(),
//                        file.getName());
//                list.add(model);
//            }
//        }
//
//        adapter = new WhatsappStatusAdapter(list, getActivity());
//        binding.mediaRecyclerView.setAdapter(adapter);
//    }
//}

