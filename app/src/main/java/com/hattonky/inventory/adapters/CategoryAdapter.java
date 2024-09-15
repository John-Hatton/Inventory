package com.hattonky.inventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hattonky.inventory.R;
import com.hattonky.inventory.data.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter to display the list of categories.
 * Each item in the list is a category with a delete button to remove it.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // List of categories to display
    private List<Category> categories = new ArrayList<>();

    // Listener interface for delete button clicks
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Called when RecyclerView needs a new ViewHolder.
     * Inflates the layout for individual category items.
     *
     * @param parent The parent view that will hold the item views.
     * @param viewType The view type of the new View (not used here).
     * @return A new instance of CategoryViewHolder.
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (category_item.xml) for each category in the list
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    /**
     * Binds the data to the ViewHolder for each category item.
     * Sets the category name and attaches a click listener to the delete button.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the current category at the specified position
        Category currentCategory = categories.get(position);

        // Set the category name in the TextView
        holder.textViewCategoryName.setText(currentCategory.getName());

        // Attach the onClick listener to the delete button
        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                // Trigger the onDeleteClick listener if it's set
                onDeleteClickListener.onDeleteClick(currentCategory);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of categories.
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Updates the list of categories and notifies the adapter to refresh the RecyclerView.
     *
     * @param categories The updated list of categories to display.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();  // Notify the adapter to refresh the list
    }

    /**
     * Interface to handle delete button clicks.
     * This allows external classes (like an activity) to define what happens when a category is deleted.
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(Category category);
    }

    /**
     * Sets the listener for delete button clicks.
     * This listener is called when the delete button is clicked on any category item.
     *
     * @param onDeleteClickListener The listener to be set.
     */
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    /**
     * ViewHolder class that holds the views for each category item in the RecyclerView.
     * This includes the TextView for the category name and the Button for deleting the category.
     */
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoryName;  // TextView to display category name
        private Button buttonDelete;  // Button to delete the category

        /**
         * Constructor for the CategoryViewHolder.
         * Initializes the views (TextView and Button) from the item layout.
         *
         * @param itemView The view representing a single item (category) in the RecyclerView.
         */
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.text_view_category_name);  // Initialize category name TextView
            buttonDelete = itemView.findViewById(R.id.button_delete_category);  // Initialize delete button
        }
    }
}
