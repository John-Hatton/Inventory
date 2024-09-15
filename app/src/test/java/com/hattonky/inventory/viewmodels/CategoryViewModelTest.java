package com.hattonky.inventory.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.repositories.CategoryRepository;

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

public class CategoryViewModelTest {

    // Rule to allow LiveData to be tested synchronously
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private CategoryViewModel categoryViewModel;

    @Mock
    private CategoryRepository categoryRepository;  // Mocked repository

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Injecting the mocked repository into the view model
        categoryViewModel = new CategoryViewModel(mock(Application.class), categoryRepository);
    }

    // Test for retrieving all categories
    @Test
    public void testGetAllCategories() {
        // Given: Simulate a list of categories to be returned from the repository
        List<Category> categories = Arrays.asList(
                new Category("Category1"),
                new Category("Category2")
        );

        // Create a LiveData object and set its value to the simulated list of categories
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        liveData.setValue(categories);

        // When the repository's getAllCategories is called, it should return the mocked LiveData
        when(categoryRepository.getAllCategories()).thenReturn(liveData);

        categoryViewModel = new CategoryViewModel(mock(Application.class), categoryRepository);

        // When: Get the categories from the ViewModel
        LiveData<List<Category>> result = categoryViewModel.getAllCategories();

        // Assert that the LiveData is not null
        assertNotNull(result);

        // Then: Observe the LiveData and verify that it contains the expected list
        result.observeForever(observedCategories -> {
            assertEquals(categories, observedCategories);
        });

        // Verify that the repository method getAllCategories was called exactly once
        verify(categoryRepository, times(2)).getAllCategories();
    }

    // Test for inserting a category
    @Test
    public void testInsertCategory() {
        // Given: Create a category name to insert
        String categoryName = "NewCategory";

        // When: Insert the category via the ViewModel
        categoryViewModel.insertCategory(categoryName);

        // Then: Verify that the repository's insertCategory method was called with the correct argument
        verify(categoryRepository, times(1)).insertCategory(categoryName);
    }

    // Test for deleting a category
    @Test
    public void testDeleteCategory() {
        // Given: Create a category to delete
        Category categoryToDelete = new Category("CategoryToDelete");

        // When: Delete the category via the ViewModel
        categoryViewModel.delete(categoryToDelete);

        // Then: Verify that the repository's delete method was called with the correct category
        verify(categoryRepository, times(1)).delete(categoryToDelete);
    }
}
