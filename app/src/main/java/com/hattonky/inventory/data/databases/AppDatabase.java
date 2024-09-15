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
 * AppDatabase is the main database class for the application.
 * It provides a singleton instance of the Room database and defines access to DAO objects.
 * This class is annotated with @Database to specify the entities (tables) and the version of the database schema.
 */
@Database(entities = {Item.class, Category.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton instance to ensure only one database object exists at a time
    private static AppDatabase instance;

    /**
     * Abstract method to get the DAO for interacting with the Item table.
     * This method is implemented by Room at runtime.
     *
     * @return The ItemDao for accessing the Item table.
     */
    public abstract ItemDao itemDao();

    /**
     * Abstract method to get the DAO for interacting with the Category table.
     * This method is implemented by Room at runtime.
     *
     * @return The CategoryDao for accessing the Category table.
     */
    public abstract CategoryDao categoryDao();  // Accessor for Category DAO

    /**
     * Synchronized method to get the singleton instance of the AppDatabase.
     * If the instance is null, the database is created using Room.databaseBuilder.
     * This ensures that only one instance of the database is created throughout the app's lifecycle.
     *
     * @param context The context from which the database is accessed.
     * @return The singleton instance of AppDatabase.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Create the database using Room's database builder
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "inventory_database")
                    .fallbackToDestructiveMigration()  // In case of schema changes, recreate the database
                    .build();
        }
        return instance;
    }
}
