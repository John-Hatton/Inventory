package com.hattonky.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hattonky.inventory.R;
import com.hattonky.inventory.adapters.ImageAdapter;

import java.io.File;
import java.util.Arrays;

/**
 * ImageGalleryActivity provides a grid view of images that the user has captured.
 * It allows users to select an image, which will be returned to the calling activity.
 */
public class ImageGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImages;  // RecyclerView for displaying images
    private ImageAdapter imageAdapter;        // Adapter to handle the display and interaction of images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        // Initialize the RecyclerView for displaying images
        recyclerViewImages = findViewById(R.id.recycler_view_images);

        // Set up a grid layout with 3 columns for the images
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 3));

        // Get the directory where images are saved (app-specific external storage directory)
        File imageDirectory = new File(getExternalMediaDirs()[0], getResources().getString(R.string.app_name));

        // Check if the image directory exists
        if (imageDirectory.exists()) {
            // Get all the image files from the directory
            File[] imageFiles = imageDirectory.listFiles();

            // If there are images available, set them to the adapter
            if (imageFiles != null && imageFiles.length > 0) {
                // Initialize the adapter with the list of images and a callback for image selection
                imageAdapter = new ImageAdapter(Arrays.asList(imageFiles), this::onImageSelected);
                recyclerViewImages.setAdapter(imageAdapter);  // Set the adapter to the RecyclerView
            } else {
                // If no images are found, display a toast message
                Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If the image directory does not exist, display a toast message
            Toast.makeText(this, "Image directory does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback method when an image is selected from the gallery.
     * Returns the selected image's file path to the calling activity.
     *
     * @param imageFile The selected image file.
     */
    private void onImageSelected(File imageFile) {
        // Prepare an intent to return the result to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("imagePath", imageFile.getAbsolutePath());  // Pass the selected image's path

        // Set the result of the activity and close the ImageGalleryActivity
        setResult(RESULT_OK, resultIntent);
        finish();  // Close the activity after the selection
    }
}
