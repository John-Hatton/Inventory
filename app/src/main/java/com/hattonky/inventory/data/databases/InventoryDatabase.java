package com.hattonky.inventory.data.databases;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.hattonky.inventory.data.dao.CategoryDao;
import com.hattonky.inventory.data.dao.ItemDao;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.data.model.Item;

/**
 * The InventoryDatabase class represents the Room database for the inventory application.
 * It defines the database configuration and serves as the main access point for the underlying database connection.
 * This class provides access to DAOs (Data Access Objects) for interacting with database entities like Item and Category.
 */
@Database(entities = {Item.class, Category.class}, version = 1)
public abstract class InventoryDatabase extends RoomDatabase {

    // Singleton instance of the InventoryDatabase
    private static volatile InventoryDatabase instance;

    // Abstract methods to provide access to DAOs
    public abstract ItemDao itemDao();  // Provides access to the Item DAO
    public abstract CategoryDao categoryDao();  // Provides access to the Category DAO

    /**
     * Gets the singleton instance of the InventoryDatabase.
     * This method ensures that only one instance of the database is created throughout the application's lifecycle.
     *
     * @param context The application context used to create or access the database.
     * @return The singleton instance of the InventoryDatabase.
     */
    public static synchronized InventoryDatabase getInstance(Context context) {
        // If no instance exists, create one
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            InventoryDatabase.class, "inventory_database")  // Define the database name
                    .fallbackToDestructiveMigration()  // Handle schema changes by destroying old data
                    .build();
        }
        return instance;  // Return the singleton instance
    }
}
