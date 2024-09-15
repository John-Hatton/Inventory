package com.hattonky.inventory.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import com.google.common.util.concurrent.ListenableFuture;
import android.Manifest;
import com.hattonky.inventory.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Activity to capture photos using the device's camera.
 * Integrates with Android's CameraX API for camera functionality.
 */
public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;  // Camera preview display
    private Button buttonCapture, buttonViewPhotos;  // Buttons for capturing and viewing photos
    private ImageCapture imageCapture;  // Image capture component for taking photos
    private File outputDirectory;  // Directory to store captured photos

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;  // Code for camera permission request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Initialize UI elements
        previewView = findViewById(R.id.preview_view);
        buttonCapture = findViewById(R.id.button_capture);
        buttonViewPhotos = findViewById(R.id.button_view_photos);

        // Set up the output directory for saving photos
        outputDirectory = getOutputDirectory();

        // Check if the app has camera permission, and start the camera if granted
        checkCameraPermission();

        // Set up the capture button to take a photo
        buttonCapture.setOnClickListener(v -> takePhoto());

        // Navigate to the ImageGalleryActivity to view captured photos
        buttonViewPhotos.setOnClickListener(v -> {
            Intent intent = new Intent(CameraActivity.this, ImageGalleryActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Initializes and starts the camera preview using CameraX.
     * Binds the camera lifecycle to the activity.
     */
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        // Listener to handle the camera provider initialization
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Set up camera preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Set up image capture
                imageCapture = new ImageCapture.Builder().build();

                // Select the back camera as default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Unbind previous use cases (if any) and bind the camera to lifecycle
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Takes a photo using the ImageCapture use case.
     * Saves the captured image to the output directory.
     */
    private void takePhoto() {
        if (imageCapture == null) return;  // Do nothing if imageCapture is not initialized

        // Create a new file to store the captured image
        File photoFile = new File(outputDirectory, "IMG_" + System.currentTimeMillis() + ".jpg");

        // Set up output options for the capture
        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Take the picture and handle success or error
        imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = Uri.fromFile(photoFile);
                        Intent intent = new Intent();
                        intent.putExtra("imagePath", savedUri.toString());  // Return the image path
                        setResult(Activity.RESULT_OK, intent);
                        finish();  // Close the activity after saving the photo
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this,
                                "Photo capture failed: " + exception.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Determines the directory where captured photos will be saved.
     * If the directory does not exist, it creates the directory.
     *
     * @return The File object representing the output directory.
     */
    private File getOutputDirectory() {
        File mediaDir = getExternalMediaDirs()[0];  // Get external media directory (e.g., SD card)
        File appDir = new File(mediaDir, getResources().getString(R.string.app_name));  // Create a subdirectory for the app
        if (!appDir.exists()) {
            appDir.mkdirs();  // Create the directory if it doesn't exist
        }
        return appDir;
    }

    /**
     * Checks if the app has the necessary camera permission.
     * Requests permission if not already granted.
     */
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        } else {
            // Start the camera if permission is granted
            startCamera();
        }
    }

    /**
     * Handles the result of the camera permission request.
     * Starts the camera if permission is granted.
     *
     * @param requestCode  The request code passed in requestPermissions.
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();  // Start the camera if permission is granted
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
