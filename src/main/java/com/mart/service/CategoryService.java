package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.CategoryDTO;

@Service
public interface CategoryService {

	// Hiển thị danh sách sản phẩm với từng thể loại.
	CategoryDTO getProductsByCategoryId(int id);

	// Tìm kiếm sản phẩm theo thể loại, khoảng giá, tên, thương hiệu.
	List<CategoryDTO> findByProducts(Integer category_id, Double minPrice, Double maxPrice, String name, String brand);

	boolean addCategory(String name);
}
