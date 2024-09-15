package com.hattonky.inventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hattonky.inventory.R;
import com.hattonky.inventory.data.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter for displaying a list of items in the inventory.
 * Each item includes a name, category, and an image.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    // List to hold the items displayed in the RecyclerView
    private List<Item> items = new ArrayList<>();

    // Listener interface to handle item click events
    private OnItemClickListener listener;

    /**
     * Constructor for the ItemAdapter.
     *
     * @param listener Listener for handling item click events.
     */
    public ItemAdapter(OnItemClickListener listener) {
        this.listener = listener;  // Initialize the listener for handling clicks
    }

    /**
     * Set a listener to handle item click events.
     *
     * @param listener The click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;  // Assign the click listener
    }

    /**
     * Interface for item click events.
     * Implemented by the class that responds to item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(Item item);  // Triggered when an item is clicked
    }

    /**
     * ViewHolder class to hold references to each view in the item layout.
     * This class manages the individual views for each item in the RecyclerView.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;       // TextView for item name
        private final TextView textViewCategory;   // TextView for item category
        private final ImageView imageViewItem;     // ImageView for item image

        /**
         * Constructor for the ItemViewHolder.
         *
         * @param itemView The view representing a single item in the RecyclerView.
         * @param listener Listener for handling click events on items.
         */
        public ItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);       // Bind the name TextView
            textViewCategory = itemView.findViewById(R.id.text_view_category); // Bind the category TextView
            imageViewItem = itemView.findViewById(R.id.image_view_item);     // Bind the ImageView

            // Set a click listener on the entire item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();  // Get the current position of the item
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick((Item) v.getTag());  // Trigger the click event using the item
                }
            });
        }
    }

    /**
     * Inflates the layout for each item in the RecyclerView.
     * This method is called when the RecyclerView needs a new ViewHolder.
     *
     * @param parent The parent view that will hold the item views.
     * @param viewType The view type of the new view (not used here).
     * @return A new instance of ItemViewHolder.
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (item_layout.xml) for each item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView, listener);  // Create and return the ViewHolder
    }

    /**
     * Binds data to the ViewHolder for each item.
     * This method is called when the RecyclerView wants to display the data for an item.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Get the current item at the specified position
        Item currentItem = items.get(position);

        // Set the item name and category in the TextViews
        holder.textViewName.setText(currentItem.getName());
        holder.textViewCategory.setText(currentItem.getCategory());

        // Use Glide to load the item image into the ImageView
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImagePath())  // Load the image from the path
                .placeholder(R.drawable.ic_placeholder_image)  // Placeholder while loading
                .into(holder.imageViewItem);  // Set the image in the ImageView

        // Set the current item as a tag for easy access in the click listener
        holder.itemView.setTag(currentItem);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return items.size();  // Return the size of the item list
    }

    /**
     * Updates the list of items displayed by the adapter.
     * This method is called to refresh the list of items shown in the RecyclerView.
     *
     * @param items The new list of items.
     */
    public void setItems(List<Item> items) {
        this.items = items;  // Update the item list
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }
}
