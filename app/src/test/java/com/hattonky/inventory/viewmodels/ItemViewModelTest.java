package com.hattonky.inventory.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hattonky.inventory.data.model.Item;
import com.hattonky.inventory.repositories.ItemRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import android.app.Application;

public class ItemViewModelTest {

    // The rule ensures that LiveData calls happen synchronously in unit tests
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ItemViewModel itemViewModel;

    @Mock
    private ItemRepository itemRepository;  // Mocked repository

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Injecting the mocked repository into the view model
        // This first call is for everything but testGetAllItems
        itemViewModel = new ItemViewModel(mock(Application.class), itemRepository);
    }

    @Test
    public void testGetAllItems() {
        // Given: Simulate a list of items to be returned from the repository
        List<Item> items = Arrays.asList(
                new Item("Item1", "Description1", "Category1", "Path1"),
                new Item("Item2", "Description2", "Category2", "Path2")
        );

        // Create a LiveData object and set its value to the simulated list of items
        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
        liveData.setValue(items);

        // When the repository's getAllItems is called, it should return the mocked LiveData
        when(itemRepository.getAllItems()).thenReturn(liveData);

        // We call the constructor again, because we want to do so after we call when above
        itemViewModel = new ItemViewModel(mock(Application.class), itemRepository);

        // When: Get the items from the ViewModel
        LiveData<List<Item>> result = itemViewModel.getAllItems();

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedItems -> {
            assertEquals(items, observedItems);
        });

        // Verify that the repository method getAllItems was called exactly once
        verify(itemRepository, times(2)).getAllItems();
    }

    @Test
    public void testGetItemsByCategory() {
        // Given: Simulate a list of items in a specific category
        List<Item> categoryItems = Arrays.asList(
                new Item("Item1", "Description1", "Category1", "Path1")
        );

        // Create a LiveData object and set its value to the simulated list of category items
        MutableLiveData<List<Item>> liveData = new MutableLiveData<>();
        liveData.setValue(categoryItems);

        // When the repository's getItemsByCategory is called, return the mocked LiveData
        when(itemRepository.getItemsByCategory("Category1")).thenReturn(liveData);

        // When: Get the items from the ViewModel by category
        LiveData<List<Item>> result = itemViewModel.getItemsByCategory("Category1");

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedItems -> {
            assertEquals(categoryItems, observedItems);
        });

        // Verify that the repository method getItemsByCategory was called exactly once
        verify(itemRepository, times(1)).getItemsByCategory("Category1");
    }

    @Test
    public void testInsertItem() {
        // Given: Create an item to insert
        Item newItem = new Item("NewItem", "NewDescription", "NewCategory", "NewPath");

        // When: Insert the item via the ViewModel
        itemViewModel.insert(newItem);

        // Then: Verify that the repository's insert method was called with the new item
        verify(itemRepository, times(1)).insert(newItem);
    }

    @Test
    public void testUpdateItem() {
        // Given: Create an item to update
        Item updatedItem = new Item("UpdatedItem", "UpdatedDescription", "UpdatedCategory", "UpdatedPath");

        // When: Update the item via the ViewModel
        itemViewModel.update(updatedItem);

        // Then: Verify that the repository's update method was called with the updated item
        verify(itemRepository, times(1)).update(updatedItem);
    }

    @Test
    public void testDeleteItem() {
        // Given: Create an item to delete
        Item itemToDelete = new Item("ItemToDelete", "Description", "Category", "Path");

        // When: Delete the item via the ViewModel
        itemViewModel.delete(itemToDelete);

        // Then: Verify that the repository's delete method was called with the item to delete
        verify(itemRepository, times(1)).delete(itemToDelete);
    }
}
