package com.djupraity.statussaver.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.djupraity.statussaver.R;
import com.djupraity.statussaver.model.WhatsAppStatusModel;

import java.util.ArrayList;

public class WhatsAppStatusAdapter extends RecyclerView.Adapter<WhatsAppStatusAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WhatsAppStatusModel> list;

    public class WhatsAppStatusViewHolder extends RecyclerView.ViewHolder {
        TextView statusTextView;

        public WhatsAppStatusViewHolder(View itemView) {
            super(itemView);
            statusTextView = itemView.findViewById(R.id.status_text_view);
        }
    }


    public WhatsAppStatusAdapter(Context context, ArrayList<WhatsAppStatusModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WhatsAppStatusModel status = list.get(position);

        if (status.getType().equals("image")) {
            // Load image using Glide
            Glide.with(context)
                    .load(status.getUrl())
                    .into(holder.statusImageView);
        } else if (status.getType().equals("video")) {
            // For video, you can add a thumbnail or an icon
            Glide.with(context)
                    .load(R.drawable.baseline_play_arrow_24) // Placeholder for video thumbnail
                    .into(holder.statusImageView);
        }

        holder.statusName.setText(status.getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImageView;
        TextView statusName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImageView = itemView.findViewById(R.id.status_image);
            statusName = itemView.findViewById(R.id.status_text_view);
        }
    }
}


//package com.djupraity.statussaver.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.djupraity.statussaver.R;
//import com.djupraity.statussaver.model.WhatsAppStatusModel;
////import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class WhatsAppStatusAdapter extends RecyclerView.Adapter<WhatsAppStatusAdapter.ViewHolder> {
//
//    private Context context;
//    private List<WhatsAppStatusModel> statusList;
//
//    public WhatsAppStatusAdapter(Context context, List<WhatsAppStatusModel> statusList) {
//        this.context = context;
//        this.statusList = statusList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_media, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        WhatsAppStatusModel status = statusList.get(position);
//
//        if (status.getType().equals("image")) {
//            // Assuming you are using Glide to load images
//            Glide.with(context)
//                    .load(status.getUrl()) // URL of the image
//                    .placeholder(R.drawable.baseline_download_24) // Optional placeholder
//                    .into(holder.statusImage);
//        } else if (status.getType().equals("video")) {
//            // Handle video view logic (e.g., display video thumbnail)
//            Glide.with(context)
//                    .load(status.getUrl()) // Assuming a method to fetch thumbnail
//                    .placeholder(R.drawable.baseline_play_arrow_24) // Optional placeholder
//                    .into(holder.statusImage);
//        } else {
//            Toast.makeText(context, "Unsupported status type", Toast.LENGTH_SHORT).show();
//        }
//
//
//        holder.statusImage.setOnClickListener(v -> {
//            Toast.makeText(context, "Image clicked", Toast.LENGTH_SHORT).show();
//            // Logic to preview image or video
//        });
//
//        // Handle download functionality here
//    }
//
//    @Override
//    public int getItemCount() {
//        return statusList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView statusImage;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            statusImage = itemView.findViewById(R.id.status_image);
//        }
//    }
//}
//package com.djupraity.statussaver.adapter;

//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.core.motion.utils.Utils;
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.djupraity.statussaver.AppUtils;
//import com.djupraity.statussaver.R;
//import com.djupraity.statussaver.databinding.ItemMediaBinding;
//import com.djupraity.statussaver.model.WhatsappStatusModel;
//
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {
//    private ArrayList<WhatsappStatusModel> list;
//    private Context context;
//    private LayoutInflater inflater;
//    private String saveFilePath = AppUtils.RootDirectoryWhatsapp + "/";
//
//    public WhatsappStatusAdapter(ArrayList<WhatsappStatusModel> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (inflater == null) {
//            inflater = LayoutInflater.from(parent.getContext());
//        }
//        return new ViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_media, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        WhatsappStatusModel item = list.get(position);
//
//        // Check if the item is a video or image
//        if (item.getUri().toString().endsWith(".mp4")) {
//            holder.binding.statusDownload.setVisibility(View.VISIBLE); // Show video icon for video files
//        } else {
//            holder.binding.statusDownload.setVisibility(View.GONE); // Hide video icon for image files
//        }
//
//        // Load the media using Glide
//        Glide.with(context)
//                .load(item.getPath())
//                .into(holder.binding.statusView);
//
//        // Set click listener (if required, add proper action)
//        holder.binding.statusView.setOnClickListener(v -> {
//            // Handle image click event (if needed)
//            AppUtils.createFileFolder();
//            final String path = item.getPath();
//            final File file = new File(path);
//            File destFile = new File(saveFilePath);
//
//            try {
//                FileUtils.copyFileToDirectory(file, destFile);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Toast.makeText(context, "Saved to: "+saveFilePath, Toast.LENGTH_SHORT).show();
//
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ItemMediaBinding binding;
//
//        public ViewHolder(ItemMediaBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
//}
