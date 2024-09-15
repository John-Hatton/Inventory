package com.hattonky.inventory.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hattonky.inventory.data.dao.ItemDao;
import com.hattonky.inventory.data.model.Item;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ItemRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ItemRepository itemRepository;

    @Mock
    private ItemDao itemDao;  // Mocked DAO

    @Mock
    private ExecutorService executorService;  // Mocked ExecutorService to avoid real threading

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize repository with mocked DAO and ExecutorService
        itemRepository = new ItemRepository(itemDao, executorService);
    }

    // Test for retrieving all items
    @Test
    public void testGetAllItems() {
        // Given: Simulate a list of items to be returned from the DAO
        List<Item> items = Arrays.asList(
                new Item("Item1", "Description1", "Category1", "Path1"),
                new Item("Item2", "Description2", "Category2", "Path2")
        );

        // Create a LiveData object and set its value to the simulated list of items
        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
        liveData.setValue(items);

        // When the DAO's getAllItems is called, it should return the mocked LiveData
        when(itemDao.getAllItems()).thenReturn(liveData);

        itemRepository = new ItemRepository(itemDao, executorService);

        // When: Get the items from the repository
        LiveData<List<Item>> result = itemRepository.getAllItems();

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedItems -> {
            assertEquals(items, observedItems);
        });

        // Verify that the DAO method getAllItems was called exactly once
        verify(itemDao, times(2)).getAllItems();
    }

    // Test for retrieving items by category
    @Test
    public void testGetItemsByCategory() {
        // Given: Simulate a list of items filtered by category
        List<Item> categoryItems = Arrays.asList(
                new Item("Item1", "Description1", "Category1", "Path1")
        );

        // Create a LiveData object and set its value to the simulated list of category items
        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
        liveData.setValue(categoryItems);

        // When the DAO's getItemsByCategory is called, it should return the mocked LiveData
        when(itemDao.getItemsByCategory("Category1")).thenReturn(liveData);

        // When: Get the items from the repository by category
        LiveData<List<Item>> result = itemRepository.getItemsByCategory("Category1");

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedItems -> {
            assertEquals(categoryItems, observedItems);
        });

        // Verify that the DAO method getItemsByCategory was called exactly once
        verify(itemDao, times(1)).getItemsByCategory("Category1");
    }

    // Test for inserting an item
    @Test
    public void testInsertItem() {
        // Given: Create an item to insert
        Item newItem = new Item("NewItem", "NewDescription", "NewCategory", "NewPath");

        // When: Insert the item via the repository
        itemRepository.insert(newItem);

        // Then: Verify that the DAO's insert method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(itemDao, never()).insert(newItem);  // DAO is never called directly, only through executor
    }

    // Test for updating an item
    @Test
    public void testUpdateItem() {
        // Given: Create an item to update
        Item updatedItem = new Item("UpdatedItem", "UpdatedDescription", "UpdatedCategory", "UpdatedPath");

        // When: Update the item via the repository
        itemRepository.update(updatedItem);

        // Then: Verify that the DAO's update method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(itemDao, never()).update(updatedItem);  // DAO is never called directly, only through executor
    }

    // Test for deleting an item
    @Test
    public void testDeleteItem() {
        // Given: Create an item to delete
        Item itemToDelete = new Item("ItemToDelete", "Description", "Category", "Path");

        // When: Delete the item via the repository
        itemRepository.delete(itemToDelete);

        // Then: Verify that the DAO's delete method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(itemDao, never()).delete(itemToDelete);  // DAO is never called directly, only through executor
    }

    // Test for deleting an item by ID
    @Test
    public void testDeleteById() {
        // Given: An ID of the item to delete
        int itemId = 1;

        // When: Delete the item by ID via the repository
        itemRepository.deleteById(itemId);

        // Then: Verify that the DAO's deleteById method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(itemDao, never()).deleteById(itemId);  // DAO is never called directly, only through executor
    }
}
