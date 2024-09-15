package com.hattonky.inventory.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hattonky.inventory.data.dao.ItemDao;
import com.hattonky.inventory.data.databases.AppDatabase;
import com.hattonky.inventory.data.model.Item;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class for managing data operations related to items.
 * Acts as the mediator between the ViewModel and the data source (database).
 * Handles background operations like inserting, updating, deleting, and querying items from the database.
 */
public class ItemRepository {

    // DAO object to interact with the Item table in the database
    private final ItemDao itemDao;

    // LiveData object containing the list of all items, observed by the UI
    private final LiveData<List<Item>> allItems;

    // ExecutorService to handle database operations on a background thread
    private final ExecutorService executorService;

    /**
     * Constructor that initializes the repository with the application context.
     * This constructor is typically used by ViewModels that require access to the application context.
     *
     * @param application The application context, used to get an instance of the database.
     */
    public ItemRepository(Application application) {
        // Get an instance of the database and DAO for accessing items
        AppDatabase database = AppDatabase.getInstance(application);
        itemDao = database.itemDao();  // Get the Item DAO
        allItems = itemDao.getAllItems();  // Fetch all items from the database
        executorService = Executors.newFixedThreadPool(2);  // Set up an Executor with two background threads
    }

    /**
     * Constructor for testing, allowing injection of a custom ItemDao and ExecutorService.
     *
     * @param itemDao         The DAO for interacting with item data.
     * @param executorService The executor for running background operations, provided for flexibility in testing.
     */
    public ItemRepository(ItemDao itemDao, ExecutorService executorService) {
        this.itemDao = itemDao;
        this.executorService = executorService;
        this.allItems = itemDao.getAllItems();  // Fetch all items from the DAO
    }

    /**
     * Returns a LiveData list of all items.
     * This LiveData is observed by the UI for any changes in the list of items.
     *
     * @return A LiveData list of all items in the database.
     */
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    /**
     * Retrieves items filtered by a given category.
     * The method returns a LiveData list of items belonging to the specified category.
     *
     * @param category The category by which to filter items.
     * @return A LiveData list of items filtered by the given category.
     */
    public LiveData<List<Item>> getItemsByCategory(String category) {
        return itemDao.getItemsByCategory(category);
    }

    /**
     * Inserts a new item into the database.
     * This operation is performed asynchronously on a background thread using ExecutorService.
     *
     * @param item The item to be inserted into the database.
     */
    public void insert(Item item) {
        executorService.execute(() -> itemDao.insert(item));  // Insert item in the background
    }

    /**
     * Updates an existing item in the database.
     * This operation is performed asynchronously on a background thread using ExecutorService.
     *
     * @param item The item to be updated in the database.
     */
    public void update(Item item) {
        executorService.execute(() -> itemDao.update(item));  // Update item in the background
    }

    /**
     * Deletes an item from the database.
     * This operation is performed asynchronously on a background thread using ExecutorService.
     *
     * @param item The item to be deleted from the database.
     */
    public void delete(Item item) {
        executorService.execute(() -> itemDao.delete(item));  // Delete item in the background
    }

    /**
     * Deletes an item by its ID.
     * This method allows deletion by a unique identifier and is executed asynchronously.
     *
     * @param itemId The unique ID of the item to be deleted.
     */
    public void deleteById(int itemId) {
        executorService.execute(() -> itemDao.deleteById(itemId));  // Delete item by ID in the background
    }
}
