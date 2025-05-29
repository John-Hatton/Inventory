package com.hattonky.inventory.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hattonky.inventory.R;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public static class User {
        public String id;
        public String username;
        public String email;
        public String role;
        public User(String id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }
    }

    private List<User> users = new ArrayList<>();
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user, int position);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < users.size()) {
            return users.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameText.setText(user.username);
        holder.emailText.setText(user.email);
        holder.roleText.setText(user.role);
        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onUserClick(user, selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, emailText, roleText;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.textUsername);
            emailText = itemView.findViewById(R.id.textEmail);
            roleText = itemView.findViewById(R.id.textRole);
        }
    }
}

