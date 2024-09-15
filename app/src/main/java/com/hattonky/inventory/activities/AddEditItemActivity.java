package com.hattonky.inventory.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hattonky.inventory.R;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.data.model.Item;
import com.hattonky.inventory.viewmodels.CategoryViewModel;
import com.hattonky.inventory.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for adding and editing items in the inventory.
 * Allows users to input item details such as name, description, category, and image.
 * Users can also select existing items to edit or create new ones.
 */
public class AddEditItemActivity extends AppCompatActivity {

    // UI components
    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerItem;
    private ImageView imageViewItem;
    private Button buttonSave, buttonCaptureChooseImage, buttonAddNewItem;

    // ViewModels for data handling
    private ItemViewModel itemViewModel;
    private CategoryViewModel categoryViewModel;

    // Variables to keep track of item state
    private int itemId = -1;  // -1 indicates a new item
    private String selectedCategory;
    private String imagePath;

    // Request codes for activity results
    private static final int CAMERA_REQUEST_CODE = 2001;
    private static final int GALLERY_REQUEST_CODE = 2002;
    private static final int INVENTORY_REQUEST_CODE = 3001;

    /**
     * Called when the activity is first created.
     * Initializes UI components and sets up ViewModels and listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize UI components
        editTextName = findViewById(R.id.edit_text_item_name);
        editTextDescription = findViewById(R.id.edit_text_item_description);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerItem = findViewById(R.id.spinner_item);
        imageViewItem = findViewById(R.id.image_view_item);
        buttonSave = findViewById(R.id.button_save);
        buttonCaptureChooseImage = findViewById(R.id.button_capture_image);
        buttonAddNewItem = findViewById(R.id.button_add_new_item);

        // Initialize ViewModels
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Set up the category spinner
        setupCategorySpinner();

        // Set up listeners for buttons and spinners
        buttonCaptureChooseImage.setOnClickListener(v -> showImageOptionsDialog());
        buttonSave.setOnClickListener(v -> saveItem());
        buttonAddNewItem.setOnClickListener(v -> clearFieldsForNewItem());

        // Handle intent data if editing an existing item
        Intent intent = getIntent();
        if (intent.hasExtra("item_id")) {
            setTitle("Edit Item");
            itemId = intent.getIntExtra("item_id", -1);
            loadItemDetails(itemId);
        } else {
            setTitle("Add Item");
        }

        // Set up delete button functionality
        setupDeleteButton();
    }

    /**
     * Sets up the category spinner by observing categories from the ViewModel.
     * Populates the spinner with category names.
     */
    private void setupCategorySpinner() {
        categoryViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                List<String> categoryNames = convertCategoryListToNames(categories);
                categoryNames.add(0, "Select Category");  // Add placeholder

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(categoryAdapter);

                if (itemId != -1) {
                    // Editing an existing item
                    itemViewModel.getAllItems().observe(this, items -> {
                        for (Item item : items) {
                            if (item.getId() == itemId) {
                                int categoryPosition = categoryAdapter.getPosition(item.getCategory());
                                spinnerCategory.setSelection(categoryPosition);
                                selectedCategory = item.getCategory();
                                setupItemSpinner(selectedCategory);
                                break;
                            }
                        }
                    });
                } else {
                    // Adding a new item
                    spinnerCategory.setSelection(0);  // Select placeholder
                }
            }
        });

        // Listener for category selection changes
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                if (!selectedCategory.equals("Select Category")) {
                    setupItemSpinner(selectedCategory);
                } else {
                    clearFieldsForNewItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    /**
     * Sets up the item spinner based on the selected category.
     * Populates the spinner with item names from the selected category.
     *
     * @param selectedCategory The category selected by the user.
     */
    private void setupItemSpinner(String selectedCategory) {
        itemViewModel.getItemsByCategory(selectedCategory).observe(this, items -> {
            List<String> itemNames = new ArrayList<>();
            itemNames.add("Add Item...");  // Placeholder for adding a new item

            for (Item item : items) {
                itemNames.add(item.getName());
            }

            ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    itemNames
            );
            itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerItem.setAdapter(itemAdapter);

            if (itemId == -1) {
                spinnerItem.setSelection(0);
            } else {
                // If editing, select the item in the spinner
                itemViewModel.getAllItems().observe(this, allItems -> {
                    for (Item item : allItems) {
                        if (item.getId() == itemId) {
                            int itemPosition = itemAdapter.getPosition(item.getName());
                            spinnerItem.setSelection(itemPosition);
                            break;
                        }
                    }
                });
            }
        });

        // Listener for item selection changes
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemName = parent.getItemAtPosition(position).toString();
                if (!selectedItemName.equals("Add Item...")) {
                    populateItemFields(selectedItemName);
                } else {
                    clearFieldsForNewItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    /**
     * Converts a list of Category objects to a list of their names.
     *
     * @param categories List of Category objects.
     * @return List of category names.
     */
    private List<String> convertCategoryListToNames(List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    /**
     * Loads item details for editing based on the item ID.
     *
     * @param itemId The ID of the item to load.
     */
    private void loadItemDetails(int itemId) {
        itemViewModel.getAllItems().observe(this, items -> {
            for (Item item : items) {
                if (item.getId() == itemId) {
                    editTextName.setText(item.getName());
                    editTextDescription.setText(item.getDescription());
                    imagePath = item.getImagePath();
                    Glide.with(this).load(imagePath).into(imageViewItem);
                    break;
                }
            }
        });
    }

    /**
     * Populates the fields with data from the selected item.
     *
     * @param itemName The name of the selected item.
     */
    private void populateItemFields(String itemName) {
        itemViewModel.getAllItems().observe(this, items -> {
            for (Item item : items) {
                if (item.getName().equals(itemName)) {
                    editTextName.setText(item.getName());
                    editTextDescription.setText(item.getDescription());
                    imagePath = item.getImagePath();
                    Glide.with(this).load(imagePath).into(imageViewItem);
                    itemId = item.getId();  // Set the itemId for editing
                    break;
                }
            }
        });
    }

    /**
     * Shows a dialog for image options: capture, choose from gallery, or select from inventory.
     */
    private void showImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image")
                .setItems(new String[]{"Capture Image", "Choose from Gallery", "Choose from Inventory"},
                        (dialog, which) -> {
                            if (which == 0) {
                                // Capture image using camera
                                Intent cameraIntent = new Intent(this, CameraActivity.class);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                            } else if (which == 1) {
                                // Choose image from gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                            } else if (which == 2) {
                                // Choose image from custom inventory gallery
                                Intent inventoryIntent = new Intent(this, ImageGalleryActivity.class);
                                startActivityForResult(inventoryIntent, INVENTORY_REQUEST_CODE);
                            }
                        })
                .show();
    }

    /**
     * Saves the item to the database. If editing, updates the existing item.
     * If adding, inserts a new item.
     */
    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = selectedCategory;

        if (name.isEmpty() || description.isEmpty() || category == null || imagePath == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemId != -1) {
            // Update existing item
            Item updatedItem = new Item(name, description, category, imagePath);
            updatedItem.setId(itemId);
            itemViewModel.update(updatedItem);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        } else {
            // Insert new item
            Item newItem = new Item(name, description, category, imagePath);
            itemViewModel.insert(newItem);
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        }

        // Refresh the item spinner to reflect changes
        setupItemSpinner(category);
    }

    /**
     * Sets up the delete button functionality.
     * Deletes the current item if in edit mode.
     */
    private void setupDeleteButton() {
        Button buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(v -> {
            if (itemId != -1) {
                // Delete the current item
                itemViewModel.delete(itemId);
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                clearFieldsForNewItem();
            } else {
                Toast.makeText(this, "No item to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Clears all input fields to allow adding a new item.
     * Resets the UI to its default state.
     */
    private void clearFieldsForNewItem() {
        editTextName.setText("");
        editTextDescription.setText("");
        imageViewItem.setImageResource(android.R.color.transparent);
        imagePath = null;
        spinnerItem.setSelection(0);
        itemId = -1;
        setTitle("Add Item");
    }

    /**
     * Handles the result from activities started for a result.
     * Processes the image selected or captured by the user.
     *
     * @param requestCode The integer request code originally supplied.
     * @param resultCode  The integer result code returned by the child activity.
     * @param data        An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                imagePath = data.getStringExtra("imagePath");
                Glide.with(this).load(imagePath).into(imageViewItem);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imagePath = selectedImageUri.toString();
                    Glide.with(this).load(imagePath).into(imageViewItem);
                }
            } else if (requestCode == INVENTORY_REQUEST_CODE) {
                imagePath = data.getStringExtra("imagePath");
                Glide.with(this).load(imagePath).into(imageViewItem);
            }
        }
    }
}
