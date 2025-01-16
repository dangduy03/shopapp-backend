package com.example.shopapp.services.category;

import java.util.List;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;

public interface ICategoryService {
	
	Category createCategory(CategoryDTO category);
	
	List<Category> getAllCategories();
	
	Category deleteCategory(long id) throws Exception;

	Category getCategoryById(long id);

	Category updateCategory(long categoryId, CategoryDTO categoryDTO);

}
