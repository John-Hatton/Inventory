package com.hattonky.inventory.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hattonky.inventory.data.model.Category;

import java.util.List;

/**
 * Data Access Object (DAO) for accessing and manipulating the categories in the database.
 * This interface provides methods to interact with the database table "categories" through Room.
 * The DAO handles SQL operations such as inserting, deleting, and querying categories.
 */
@Dao
public interface CategoryDao {

    /**
     * Inserts a new category into the database.
     * If the category already exists, it will be ignored by default (conflict resolution not specified).
     *
     * @param category The category to be inserted.
     */
    @Insert
    void insert(Category category);

    /**
     * Deletes the specified category from the database.
     * This will remove the category entirely from the "categories" table.
     *
     * @param category The category to be deleted.
     */
    @Delete
    void delete(Category category);

    /**
     * Queries all categories from the database and orders them by name in ascending order.
     * This method returns a LiveData object, which allows automatic updates to any observers
     * when the data in the "categories" table changes.
     *
     * @return A LiveData list of all categories sorted by name.
     */
    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();
}
