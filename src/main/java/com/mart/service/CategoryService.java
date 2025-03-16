package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.CategoryDTO;
import com.mart.entity.Category;

@Service
public interface CategoryService {

	// Display product list by category
	CategoryDTO getProductsByCategoryId(long categoryId);

	// Search products by category, price range, name, brand
	List<CategoryDTO> findByProducts(Long category_id, Double minPrice, Double maxPrice, String name, String brand);

	// Add Category
	Category addCategory(String name);

	// Display a list of categories
	List<CategoryDTO> getCategory();
	
	// Update Category
	CategoryDTO updateCategory(long categoryId, String name);
	
	// Delete Category
	boolean deleteCategory(long categoryId);
}
