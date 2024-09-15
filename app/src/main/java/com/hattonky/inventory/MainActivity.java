package com.hattonky.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;

import com.hattonky.inventory.activities.AddEditItemActivity;
import com.hattonky.inventory.activities.CameraActivity;
import com.hattonky.inventory.activities.AddCategoryActivity;
import com.hattonky.inventory.activities.ImageGalleryActivity;
import com.hattonky.inventory.adapters.ItemAdapter;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.data.model.Item;
import com.hattonky.inventory.viewmodels.CategoryViewModel;
import com.hattonky.inventory.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private Spinner categorySpinner;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ItemViewModel itemViewModel;
    private CategoryViewModel categoryViewModel;
    private DrawerLayout drawerLayout;
    private List<String> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar and Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks (existing code)
        setupNavigationMenu(navigationView);

        // Existing RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ItemAdapter(this);
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, items -> adapter.setItems(items));

        // Setup CategoryViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Spinner setup for category selection
        categorySpinner = findViewById(R.id.spinner_category);
        fetchCategories();

        categoryViewModel.getAllCategories().observe(this, categories -> {
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("All");  // Add default "All" category

            for (Category category : categories) {
                categoryNames.add(category.getName());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categoryNames);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(spinnerAdapter);
        });

        // Listen for category selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                if (category.equals("All")) {
                    itemViewModel.getAllItems().observe(MainActivity.this, items -> adapter.setItems(items));
                } else {
                    itemViewModel.getItemsByCategory(category).observe(MainActivity.this, items -> adapter.setItems(items));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Item click listener to navigate to AddEditItemActivity with the selected item
        adapter.setOnItemClickListener(this);
    }

    private void setupNavigationMenu(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_main) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_add_edit) {
                Intent addEditIntent = new Intent(MainActivity.this, AddEditItemActivity.class);
                startActivity(addEditIntent);
                return true;
            } else if (id == R.id.nav_camera) {
                Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(cameraIntent);
                return true;
            } else if (id == R.id.nav_add_category) {
                Intent categoryIntent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(categoryIntent);
                return true;
            } else if (id == R.id.nav_image_gallery) {
                Intent imageGalleryIntent = new Intent(MainActivity.this, ImageGalleryActivity.class);
                startActivity(imageGalleryIntent);
                return true;
            }
            return false;
        });
    }


    private void fetchCategories() {
        // Observe categories from the CategoryViewModel
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoriesList.clear();  // Clear any previous categories
                categoriesList.add("All");  // Add the "All" option at the top
                for (Category category : categories) {
                    categoriesList.add(category.getName());  // Add each category name to the list
                }

                // Update the spinner with the fetched categories
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        MainActivity.this, android.R.layout.simple_spinner_item, categoriesList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(spinnerAdapter);
            }
        });
    }



    @Override
    public void onItemClick(Item item) {
        // Navigate to AddEditItemActivity with the selected item
        Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
        int myId = item.getId();
        intent.putExtra("item_id", myId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
