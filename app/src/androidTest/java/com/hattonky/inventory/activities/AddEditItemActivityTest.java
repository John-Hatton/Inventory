package com.hattonky.inventory.activities;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.hattonky.inventory.R;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.data.model.Item;
import com.hattonky.inventory.viewmodels.CategoryViewModel;
import com.hattonky.inventory.viewmodels.ItemViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AddEditItemActivityTest {

    @Rule
    public ActivityTestRule<AddEditItemActivity> activityRule =
            new ActivityTestRule<>(AddEditItemActivity.class, true, false);

    @Mock
    ItemViewModel itemViewModel;

    @Mock
    CategoryViewModel categoryViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewItem() {
        // Launch the AddEditItemActivity in 'Add' mode
        activityRule.launchActivity(new Intent());

        // Mock the categories returned from CategoryViewModel
        when(categoryViewModel.getAllCategories()).thenReturn(
                (LiveData<List<Category>>) Arrays.asList(new Category("Electronics"), new Category("Furniture")));

        // Check if the category spinner is populated and select the first item
        onView(withId(R.id.spinner_category)).perform(click());
        Espresso.onData(withText("Electronics")).perform(click());

        // Enter item details
        onView(withId(R.id.edit_text_item_name)).perform(typeText("New Item"));
        onView(withId(R.id.edit_text_item_description)).perform(typeText("New Item Description"));

        // Save the item
        onView(withId(R.id.button_save)).perform(click());

        // Validate that the ItemViewModel's insert method was called with the correct data
        Item newItem = new Item("New Item", "New Item Description", "Electronics", null);
        verify(itemViewModel).insert(newItem);
    }
}
