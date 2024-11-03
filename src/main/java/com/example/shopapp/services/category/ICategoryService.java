package com.example.shopapp.services.category;

import java.util.List;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;

public interface ICategoryService {
	
	Category createCategory(CategoryDTO category);
	
//	Category getCategoryById(Long id);
	
	List<Category> getAllCategories();
	
//	Category updateCategory(Long CategoryId, CategoryDTO category);
	
	Category deleteCategory(long id) throws Exception;

	Category getCategoryById(long id);

	Category updateCategory(long categoryId, CategoryDTO categoryDTO);

}
