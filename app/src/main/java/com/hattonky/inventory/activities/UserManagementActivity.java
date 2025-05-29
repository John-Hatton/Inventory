package com.hattonky.inventory.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import com.hattonky.inventory.R;
import com.hattonky.inventory.adapters.UserAdapter;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import com.hattonky.inventory.data.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class UserManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        Button buttonEdit = findViewById(R.id.buttonEditUser);
        Button buttonDelete = findViewById(R.id.buttonDeleteUser);

        // Remove the placeholder text if present
        android.view.View placeholder = findViewById(R.id.textViewUserManagementTitle).getRootView().findViewById(android.R.id.text1);
        if (placeholder != null) placeholder.setVisibility(android.view.View.GONE);

        UserAdapter adapter = new UserAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch users from API
        ApiClient.getAllUsers(this, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    java.util.List<UserAdapter.User> users = new java.util.ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        users.add(new UserAdapter.User(
                            obj.optString("id"),
                            obj.optString("username"),
                            obj.optString("email"),
                            obj.optString("role")
                        ));
                    }
                    runOnUiThread(() -> adapter.setUsers(users));
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(UserManagementActivity.this, "Failed to parse users", Toast.LENGTH_LONG).show());
                }
            }
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    AlertDialog dialog = new AlertDialog.Builder(UserManagementActivity.this)
                        .setTitle("Failed to fetch users")
                        .setMessage(e.getMessage())
                        .setPositiveButton("OK", null)
                        .create();
                    dialog.show();
                });
            }
        });

        adapter.setOnUserClickListener((user, position) -> {
            buttonEdit.setEnabled(true);
            buttonDelete.setEnabled(true);
        });

        buttonEdit.setOnClickListener(v -> {
            UserAdapter.User selected = adapter.getSelectedUser();
            if (selected != null) {
                showEditUserDialog(selected, adapter);
            }
        });
        buttonDelete.setOnClickListener(v -> {
            UserAdapter.User selected = adapter.getSelectedUser();
            if (selected != null) {
                new AlertDialog.Builder(this)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete user '" + selected.username + "'?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        ApiClient.deleteUserById(this, selected.id, new ApiClient.ApiCallback() {
                            @Override
                            public void onSuccess(String response) {
                                runOnUiThread(() -> {
                                    Toast.makeText(UserManagementActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                                    // Refresh user list
                                    recreate();
                                });
                            }
                            @Override
                            public void onFailure(IOException e) {
                                runOnUiThread(() -> {
                                    AlertDialog dialog = new AlertDialog.Builder(UserManagementActivity.this)
                                        .setTitle("Delete failed")
                                        .setMessage(e.getMessage())
                                        .setPositiveButton("OK", null)
                                        .create();
                                    dialog.show();
                                });
                            }
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            }
        });
    }

    private void showEditUserDialog(UserAdapter.User user, UserAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit User");
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        EditText editUsername = dialogView.findViewById(R.id.editUsername);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        EditText editRole = dialogView.findViewById(R.id.editRole);
        editUsername.setText(user.username);
        editEmail.setText(user.email);
        editRole.setText(user.role);
        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newRole = editRole.getText().toString();
            ApiClient.updateUserById(this, user.id, newRole, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        user.role = newRole;
                        adapter.notifyDataSetChanged();
                        Toast.makeText(UserManagementActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                    });
                }
                @Override
                public void onFailure(IOException e) {
                    runOnUiThread(() -> {
                        AlertDialog dialog = new AlertDialog.Builder(UserManagementActivity.this)
                            .setTitle("Update failed")
                            .setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create();
                        dialog.show();
                    });
                }
            });
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
