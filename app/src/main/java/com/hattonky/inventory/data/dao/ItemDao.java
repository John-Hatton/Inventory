package com.hattonky.inventory.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hattonky.inventory.data.model.Item;

import java.util.List;

/**
 * Data Access Object (DAO) for interacting with the "items" table in the database.
 * Provides methods for inserting, updating, deleting, and querying item records in the Room database.
 */
@Dao
public interface ItemDao {

    /**
     * Inserts a new item into the database.
     * If the item already exists, Room will handle conflict resolution (default behavior).
     *
     * @param item The item to be inserted.
     */
    @Insert
    void insert(Item item);

    /**
     * Updates an existing item in the database.
     * The item is matched based on its unique identifier (ID).
     *
     * @param item The item to be updated.
     */
    @Update
    void update(Item item);

    /**
     * Deletes a specific item from the database.
     *
     * @param item The item to be deleted.
     */
    @Delete
    void delete(Item item);

    /**
     * Deletes an item from the database by its unique identifier (ID).
     *
     * @param itemId The ID of the item to be deleted.
     */
    @Query("DELETE FROM items WHERE id = :itemId")
    void deleteById(int itemId);

    /**
     * Retrieves all items from the database, ordered by their name in ascending order.
     * Returns a LiveData list, meaning the UI can observe changes in the list automatically.
     *
     * @return A LiveData list of all items sorted by name.
     */
    @Query("SELECT * FROM items ORDER BY name ASC")
    LiveData<List<Item>> getAllItems();

    /**
     * Retrieves items that belong to a specific category, ordered by name in ascending order.
     * Returns a LiveData list of items for real-time updates in the UI.
     *
     * @param category The category by which to filter items.
     * @return A LiveData list of items filtered by the specified category.
     */
    @Query("SELECT * FROM items WHERE category = :category ORDER BY name ASC")
    LiveData<List<Item>> getItemsByCategory(String category);
}
