package com.hattonky.inventory.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.hattonky.inventory.data.dao.CategoryDao;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.data.databases.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class for managing data operations related to categories.
 * Acts as a single source of truth for category-related data, abstracting access to the data source (database).
 * It also provides a clean API for accessing category data for the rest of the application.
 */
public class CategoryRepository {

    // DAO object to access Category data from the database
    private CategoryDao categoryDao;

    // LiveData list of all categories, observed by the UI
    private LiveData<List<Category>> allCategories;

    // ExecutorService for executing database operations on a background thread
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Constructor that initializes the repository with the application context.
     * This version of the constructor is typically used in ViewModels that require the application context.
     *
     * @param application The application context, used to get an instance of the database.
     */
    public CategoryRepository(Application application) {
        // Get a reference to the database and category DAO
        AppDatabase database = AppDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();  // Fetch all categories from the DAO
    }

    /**
     * Constructor used primarily for testing purposes, allowing injection of a mock DAO and ExecutorService.
     *
     * @param categoryDao      The mock DAO for testing.
     * @param executorService  The executor service for background operations, provided externally for flexibility in testing.
     */
    public CategoryRepository(CategoryDao categoryDao, ExecutorService executorService) {
        this.categoryDao = categoryDao;
        this.executorService = executorService;
        this.allCategories = categoryDao.getAllCategories();  // Fetch all categories from the DAO
    }

    /**
     * Returns a LiveData list of all categories.
     * This LiveData is observed by the UI for any changes in the list of categories.
     *
     * @return A LiveData list of all categories in the database.
     */
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    /**
     * Inserts a new category into the database.
     * The operation is performed asynchronously on a background thread using ExecutorService.
     *
     * @param categoryName The name of the new category to be inserted.
     */
    public void insertCategory(String categoryName) {
        executorService.execute(() -> categoryDao.insert(new Category(categoryName)));
    }

    /**
     * Deletes a category from the database.
     * The operation is performed asynchronously on a background thread using ExecutorService.
     *
     * @param category The category to be deleted from the database.
     */
    public void delete(Category category) {
        executorService.execute(() -> categoryDao.delete(category));
    }
}
