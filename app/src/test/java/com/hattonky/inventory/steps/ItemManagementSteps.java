package com.hattonky.inventory.steps;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.hattonky.inventory.activities.AddEditItemActivity;
import com.hattonky.inventory.MainActivity;
import com.hattonky.inventory.R;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class ItemManagementSteps {

    @Before
    public void setUp() {
        // Initialize any required resources before each scenario
    }

    @Given("I am on the Add Item screen")
    public void i_am_on_the_add_item_screen() {
        ActivityScenarioRule<AddEditItemActivity> activityRule =
                new ActivityScenarioRule<>(AddEditItemActivity.class);
    }

    @When("I enter valid item details")
    public void i_enter_valid_item_details() {
        onView(withId(R.id.edit_text_item_name))
                .perform(typeText("New Item"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_item_description))
                .perform(typeText("Item Description"), closeSoftKeyboard());
        // Select a category if necessary
    }

    @And("I save the item")
    public void i_save_the_item() {
        onView(withId(R.id.button_save)).perform(click());
    }

    @Then("the item should be added to the inventory")
    public void the_item_should_be_added_to_the_inventory() {
        // Verify the item appears in the inventory list
        onView(withText("New Item")).check(matches(isDisplayed()));
    }

    // Implement other steps similarly...
}
