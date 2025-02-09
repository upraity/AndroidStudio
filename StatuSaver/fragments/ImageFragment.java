package com.djupraity.status.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djupraity.status.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<File> mediaFiles = new ArrayList<>();
    private StatusAdapter statusAdapter;
    private TextView noContentMessage;
    public static ImageFragment getInstance() {
        return new ImageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        noContentMessage = view.findViewById(R.id.noContentMessage);
        // Load media files (implement your media loading logic)
        loadMedia();

        return view;
    }

    private void loadMedia() {
        mediaFiles = new ArrayList<>(); // Ensure the list is initialized

        // Get the Statuses directory path
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses");

        // Check if directory exists
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) { // Ensure files exist
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        mediaFiles.add(file);
                    }
                }

                // Show or hide the noContentMessage only once, after the loop
                if (mediaFiles.isEmpty()) {
                    noContentMessage.setVisibility(View.VISIBLE);
                } else {
                    noContentMessage.setVisibility(View.GONE);
                }
            } else {
                noContentMessage.setVisibility(View.VISIBLE); // No files in directory
            }
        } else {
            noContentMessage.setVisibility(View.VISIBLE); // Directory does not exist
        }

        // Set adapter for the RecyclerView
        statusAdapter = new StatusAdapter(mediaFiles);
        recyclerView.setAdapter(statusAdapter);
    }


    // Adapter class to handle the images and buttons
    public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
        private ArrayList<File> mediaFiles;

        public StatusAdapter(ArrayList<File> mediaFiles) {
            this.mediaFiles = mediaFiles;
        }

        @NonNull
        @Override
        public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_status, parent, false);
            return new StatusViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
            File mediaFile = mediaFiles.get(position);

            // Load the image into ImageView (using Glide or Picasso can be more efficient)
            holder.imageView.setImageURI(Uri.fromFile(mediaFile));

            // Handle download button click
            holder.downloadButton.setOnClickListener(v -> downloadImage(mediaFile));

            // Handle share button click
            holder.shareButton.setOnClickListener(v -> shareImage(mediaFile));
        }

        @Override
        public int getItemCount() {
            return mediaFiles.size();
        }

        public class StatusViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageButton downloadButton, shareButton;

            public StatusViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                downloadButton = itemView.findViewById(R.id.downloadButton);
                shareButton = itemView.findViewById(R.id.shareButton);
            }
        }
    }

    private void downloadImage(File file) {
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/StatusSaver/Whatsapp Business/Image");
        if (!directory.exists()) {
            directory.mkdirs(); // Create the folder if it doesn't exist
        }

        File savedFile = new File(directory, file.getName());
        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(getContext(), "Image saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage(File file) {
        // Get the URI from FileProvider
        Uri uri = FileProvider.getUriForFile(getContext(),
                getContext().getPackageName() + ".provider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission for the URI

        startActivity(Intent.createChooser(shareIntent, "Share image via"));
    }
}
