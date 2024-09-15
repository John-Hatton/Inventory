package com.hattonky.inventory.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hattonky.inventory.data.model.Item;
import com.hattonky.inventory.repositories.ItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private final ItemRepository repository;
    private final LiveData<List<Item>> allItems;

    // Constructor, initializes the repository and loads all items
    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems(); // Observe all items in the inventory

    }

    public ItemViewModel(@NonNull Application application, ItemRepository itemRepository) {
        super(application);
        this.repository = itemRepository;
        allItems = repository.getAllItems();
    }

    // Returns a LiveData object that the UI can observe to get the list of all items
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    // Returns a LiveData object that the UI can observe to get items filtered by category
    public LiveData<List<Item>> getItemsByCategory(String category) {
        return repository.getItemsByCategory(category);
    }

    // Insert a new item into the repository
    public void insert(Item item) {
        repository.insert(item);
    }

    // Update an existing item in the repository
    public void update(Item item) {
        repository.update(item);
    }

    // Delete an item from the repository
    public void delete(Item item) {
        repository.delete(item);
    }

    public void delete(int itemId) {
        repository.deleteById(itemId);  // Call repository to delete item by ID
    }
}