package com.mart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mart.dto.CategoryDTO;
import com.mart.dto.ProductDTO;
import com.mart.entity.Category;
import com.mart.entity.Product;
import com.mart.repository.CategoryRepository;
import com.mart.repository.ProductRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	// Display product list by category
	@Override
	public CategoryDTO getProductsByCategoryId(long categoryId) {

		try {

			Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

			if (categoryOptional.isEmpty()) {
				throw new RuntimeException("Category not found!");
			}

			Category category = categoryOptional.get();

			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());

			List<ProductDTO> productDTOs = new ArrayList<>();

			for (Product data : category.getProducts()) {

				ProductDTO productDTO = new ProductDTO(data);
				productDTOs.add(productDTO);
			}

			categoryDTO.setProductDTOs(productDTOs);

			return categoryDTO;

		} catch (Exception e) {
			throw new RuntimeException("Error displaying product list by category!", e);
		}

	}

	// Search products by category, price range, name, brand
	@Override
	public List<CategoryDTO> findByProducts(Long category_id, Double minPrice, Double maxPrice, String name,
			String brand) {
		try {
			List<Product> products = productRepository.findByCategoryIdAndPriceRangeAndNameAndBrand(category_id,
					minPrice, maxPrice, name, brand);

			if (products.isEmpty()) {
				return Collections.emptyList(); // Return empty list instead of null
			}

			Map<Long, CategoryDTO> categoryMap = new HashMap<>(); // Use Map to group by category_id

			for (Product data : products) {
				long catId = data.getCategory().getId(); // Get the category_id of the product

				// If CategoryDTO is not in Map then create new one
				CategoryDTO categoryDTO = categoryMap.getOrDefault(catId, new CategoryDTO());
				categoryDTO.setName(data.getCategory().getName());
				categoryDTO.setId(data.getCategory().getId()); // If the product list does not exist, create a new one
				if (categoryDTO.getProductDTOs() == null) {
					categoryDTO.setProductDTOs(new ArrayList<>());
				}

				// Product -> ProductDTO
				ProductDTO productDTO = new ProductDTO(data);

				// Add products to CategoryDTO list
				categoryDTO.getProductDTOs().add(productDTO);

				// Add to Map if not already there
				categoryMap.put(catId, categoryDTO);
			}

			// Returns a list of CategoryDTOs
			return new ArrayList<>(categoryMap.values());

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return Collections.emptyList(); // Avoid returning null
		}
	}

	@Override
	public Category addCategory(String name) {
		try {
			if (categoryRepository.findByName(name).isPresent()) {
				throw new RuntimeException("The category already exists!");
			}

			Category category = new Category();
			category.setName(name);

			return categoryRepository.save(category);

		} catch (Exception e) {
			throw new RuntimeException("Error adding category!", e);
		}
	}

	@Override
	public List<CategoryDTO> getCategory() {

		try {

			List<Category> categories = categoryRepository.findAll();
			List<CategoryDTO> categoryDTOs = new ArrayList<>();
			for (Category data : categories) {

				categoryDTOs.add(CategoryDTO.toBasicCategoryDTO(data));

			}

			return categoryDTOs;

		} catch (Exception e) {
			throw new RuntimeException("Error displaying category list!", e);
		}

	}

	@Override
	public CategoryDTO updateCategory(long categoryId, String name) {

		try {
			Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
			if (categoryOptional.isEmpty()) {
				throw new RuntimeException("Error type not found with Id " + categoryId);
			}
			Category category = categoryOptional.get();
			category.setName(name);
			Category saveCategory = categoryRepository.save(category);
			return CategoryDTO.toBasicCategoryDTO(saveCategory);
		} catch (Exception e) {
			throw new RuntimeException("Error update category!", e);
		}

	}

	@Override
	public boolean deleteCategory(long categoryId) {

		try {
			categoryRepository.deleteById(categoryId);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Error delete category!", e);
		}

	}

}
