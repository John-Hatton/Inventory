package com.hattonky.inventory.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.hattonky.inventory.data.model.Category;
import com.hattonky.inventory.repositories.CategoryRepository;
import com.hattonky.inventory.repositories.ItemRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository repository;
    private LiveData<List<Category>> allCategories;

    public CategoryViewModel(Application application) {
        super(application);
        repository = new CategoryRepository(application);
        allCategories = repository.getAllCategories();
    }

    public CategoryViewModel(@NonNull Application application, CategoryRepository categoryRepository) {
        super(application);
        this.repository = categoryRepository;
        allCategories = repository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insertCategory(String categoryName) {
        repository.insertCategory(categoryName);
    }

    public void delete(Category category) {
        repository.delete(category);
    }
}
