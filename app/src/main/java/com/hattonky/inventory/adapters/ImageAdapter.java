package com.hattonky.inventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hattonky.inventory.R;

import java.io.File;
import java.util.List;

/**
 * RecyclerView Adapter for displaying a list of images from the file system.
 * Each image is displayed using an ImageView inside the RecyclerView.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    // List of image files to display
    private final List<File> imageFiles;

    // Listener for handling item clicks
    private final OnItemClickListener onItemClickListener;

    /**
     * Interface to handle click events on images.
     * Implemented by the class that needs to respond to image clicks.
     */
    public interface OnItemClickListener {
        void onItemClick(File imageFile);  // Triggered when an image is clicked
    }

    /**
     * Constructor for the ImageAdapter.
     *
     * @param imageFiles The list of image files to display.
     * @param onItemClickListener Listener to handle click events on each image.
     */
    public ImageAdapter(List<File> imageFiles, OnItemClickListener onItemClickListener) {
        this.imageFiles = imageFiles;  // Initialize list of images
        this.onItemClickListener = onItemClickListener;  // Initialize click listener
    }

    /**
     * Called when RecyclerView needs a new ViewHolder.
     * Inflates the layout for individual image items.
     *
     * @param parent The parent view that will hold the item views.
     * @param viewType The view type of the new View (not used here).
     * @return A new instance of ImageViewHolder.
     */
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (image_item.xml) for each image
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    /**
     * Binds the data to the ViewHolder for each image item.
     * Loads the image from a file into the ImageView using Glide.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Get the current image file at the specified position
        File imageFile = imageFiles.get(position);

        // Use Glide to load the image into the ImageView
        Glide.with(holder.itemView.getContext())
                .load(imageFile)  // Load the image from the file
                .into(holder.imageView);  // Display it in the ImageView

        // Set a click listener on the item to trigger the OnItemClickListener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(imageFile));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of images.
     */
    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    /**
     * ViewHolder class that holds the views for each image item in the RecyclerView.
     * This includes the ImageView that displays the image.
     */
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;  // ImageView to display the image

        /**
         * Constructor for the ImageViewHolder.
         * Initializes the ImageView from the item layout.
         *
         * @param itemView The view representing a single item (image) in the RecyclerView.
         */
        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);  // Initialize ImageView
        }
    }
}
