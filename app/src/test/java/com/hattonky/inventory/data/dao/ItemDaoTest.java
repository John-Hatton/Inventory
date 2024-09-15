package com.hattonky.inventory.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.hattonky.inventory.data.databases.AppDatabase;
import com.hattonky.inventory.data.model.Item;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ItemDaoTest {

    private AppDatabase database;
    private ItemDao itemDao;

    // Rule to make LiveData updates synchronous in unit tests
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(), AppDatabase.class)
                .allowMainThreadQueries()  // Allows Room operations on the main thread for testing
                .build();
        itemDao = database.itemDao();
    }

    @After
    public void tearDown() {
        // Close the database when the test finishes
        database.close();
    }

    @Test
    public void testInsertAndGetAllItems() throws InterruptedException {
        // Given: Create a new item to insert
        Item item = new Item("TestItem", "TestDescription", "TestCategory", "TestPath");

        // When: Insert the item into the database
        itemDao.insert(item);

        // Then: Query all items and check if the inserted item is returned
        LiveData<List<Item>> allItems = itemDao.getAllItems();
        List<Item> itemList = getOrAwaitValue(allItems);

        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("TestItem", itemList.get(0).getName());
    }

    @Test
    public void testUpdateItem() throws InterruptedException {
        // Given: Insert an item and then update its name
        Item item = new Item("OriginalName", "Description", "Category", "Path");
        itemDao.insert(item);

        item.setName("UpdatedName");
        itemDao.update(item);

        // Then: Check if the item was updated
        LiveData<List<Item>> allItems = itemDao.getAllItems();
        List<Item> itemList = getOrAwaitValue(allItems);

        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("UpdatedName", itemList.get(0).getName());
    }

    @Test
    public void testDeleteItem() throws InterruptedException {
        // Given: Insert an item and then delete it
        Item item = new Item("DeleteMe", "Description", "Category", "Path");
        itemDao.insert(item);

        itemDao.delete(item);

        // Then: Query all items and ensure the list is empty
        LiveData<List<Item>> allItems = itemDao.getAllItems();
        List<Item> itemList = getOrAwaitValue(allItems);

        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());
    }

    @Test
    public void testGetItemsByCategory() throws InterruptedException {
        // Given: Insert items in two different categories
        Item item1 = new Item("Item1", "Description", "Category1", "Path");
        Item item2 = new Item("Item2", "Description", "Category2", "Path");
        itemDao.insert(item1);
        itemDao.insert(item2);

        // When: Query by category
        LiveData<List<Item>> category1Items = itemDao.getItemsByCategory("Category1");
        List<Item> itemList = getOrAwaitValue(category1Items);

        // Then: Check if only the item from "Category1" is returned
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("Category1", itemList.get(0).getCategory());
    }

    // Utility method to get LiveData value synchronously
    private <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) data[0];
    }
}
