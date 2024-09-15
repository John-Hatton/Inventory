package com.hattonky.inventory.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hattonky.inventory.R;
import com.hattonky.inventory.adapters.CategoryAdapter;
import com.hattonky.inventory.viewmodels.CategoryViewModel;

/**
 * Activity for managing categories in the inventory system.
 * This activity allows the user to add new categories and delete existing ones.
 */
public class AddCategoryActivity extends AppCompatActivity {

    // Views for user input and interaction
    private EditText editTextCategoryName;  // Input field for entering a new category name
    private Button buttonAddCategory;       // Button to trigger adding a new category
    private RecyclerView recyclerViewCategories;  // RecyclerView to display the list of categories

    // ViewModel and Adapter for handling categories
    private CategoryViewModel categoryViewModel;  // ViewModel to manage category data
    private CategoryAdapter categoryAdapter;      // Adapter for binding categories to the RecyclerView

    /**
     * Called when the activity is first created.
     * This method sets up the views and binds data to the RecyclerView.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Initialize views from the layout
        editTextCategoryName = findViewById(R.id.edit_text_category_name);  // Input field for category name
        buttonAddCategory = findViewById(R.id.button_add_category);        // Button to add a new category
        recyclerViewCategories = findViewById(R.id.recycler_view_categories); // RecyclerView for displaying categories

        // Setup the RecyclerView with a linear layout and the CategoryAdapter
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setHasFixedSize(true);  // Performance optimization when RecyclerView size is fixed
        categoryAdapter = new CategoryAdapter();  // Initialize the CategoryAdapter
        recyclerViewCategories.setAdapter(categoryAdapter);  // Bind the adapter to the RecyclerView

        // Initialize the ViewModel for managing category data
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Observe the category data and update the RecyclerView when the list of categories changes
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.setCategories(categories);  // Update the adapter with new category data
        });

        // Set a click listener for the "Add Category" button
        buttonAddCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString().trim();  // Get the category name from the input
            if (!categoryName.isEmpty()) {
                // Insert the new category into the database via the ViewModel
                categoryViewModel.insertCategory(categoryName);
                Toast.makeText(AddCategoryActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                editTextCategoryName.setText("");  // Clear the input field after adding the category
            } else {
                // Show a toast if the input field is empty
                Toast.makeText(AddCategoryActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle category deletion via the CategoryAdapter's delete button
        categoryAdapter.setOnDeleteClickListener(category -> {
            // Delete the category using the ViewModel
            categoryViewModel.delete(category);
            Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
        });
    }
}
