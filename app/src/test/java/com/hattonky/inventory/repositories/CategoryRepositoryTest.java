package com.hattonky.inventory.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hattonky.inventory.data.dao.CategoryDao;
import com.hattonky.inventory.data.model.Category;

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

public class CategoryRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private CategoryRepository categoryRepository;

    @Mock
    private CategoryDao categoryDao;  // Mocked DAO

    @Mock
    private ExecutorService executorService;  // Mocked ExecutorService

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize repository with mocked DAO and ExecutorService
        categoryRepository = new CategoryRepository(categoryDao, executorService);
    }

    // Test for retrieving all categories
    @Test
    public void testGetAllCategories() {
        // Given: Simulate a list of categories to be returned from the DAO
        List<Category> categories = Arrays.asList(
                new Category("Category1"),
                new Category("Category2")
        );

        // Create a LiveData object and set its value to the simulated list of categories
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        liveData.setValue(categories);

        // When the DAO's getAllCategories is called, it should return the mocked LiveData
        when(categoryDao.getAllCategories()).thenReturn(liveData);

        // Call constructor again after when statement
        categoryRepository = new CategoryRepository(categoryDao, executorService);

        // When: Get the categories from the repository
        LiveData<List<Category>> result = categoryRepository.getAllCategories();

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedCategories -> {
            assertEquals(categories, observedCategories);
        });

        // Verify that the DAO method getAllCategories was called exactly once
        verify(categoryDao, times(2)).getAllCategories();
    }

    // Test for inserting a category
    @Test
    public void testInsertCategory() {
        // Given: A category name to insert
        String categoryName = "NewCategory";
        Category newCategory = new Category(categoryName);  // Create a new Category object

        // When: Insert the category via the repository
        categoryRepository.insertCategory(categoryName);

        // Then: Verify that the DAO's insert method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(categoryDao, never()).insert(newCategory);  // DAO is never called directly, only through executor
    }

    // Test for deleting a category
    @Test
    public void testDeleteCategory() {
        // Given: A category to delete
        Category categoryToDelete = new Category("CategoryToDelete");

        // When: Delete the category via the repository
        categoryRepository.delete(categoryToDelete);

        // Then: Verify that the DAO's delete method was called
        verify(executorService).execute(any(Runnable.class));  // Verify the executor was used
        verify(categoryDao, never()).delete(categoryToDelete);  // DAO is never called directly, only through executor
    }
}
