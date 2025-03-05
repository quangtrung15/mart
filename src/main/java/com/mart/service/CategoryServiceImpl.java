package com.mart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

	// Hiển thị danh sách sản phẩm với từng thể loại.
	@Override
	public CategoryDTO getProductsByCategoryId(int id) {

		try {

			Category category = categoryRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found!"));

			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());

			List<ProductDTO> productDTOs = new ArrayList<>();

			for (Product data : category.getProducts()) {
				ProductDTO productDTO = new ProductDTO();
				productDTO.setId(data.getId());
				productDTO.setImage(data.getImage());
				productDTO.setName(data.getName());
				productDTO.setPrice(data.getPrice());
				productDTO.setPromo(data.getPromo());
				productDTO.setQuantity(data.getQuantity());
				productDTO.setDescription(data.getDescription());
				productDTO.setStatus(data.getStatus());
				productDTOs.add(productDTO);
			}

			categoryDTO.setProductDTOs(productDTOs);

			return categoryDTO;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm theo thể loại", e);
		}

	}

	// Tìm kiếm sản phẩm theo thể loại, khoảng giá, tên, thương hiệu.
	@Override
	public List<CategoryDTO> findByProducts(Integer category_id, Double minPrice, Double maxPrice, String name,
			String brand) {
		try {
			List<Product> products = productRepository.findByCategoryIdAndPriceRangeAndNameAndBrand(category_id,
					minPrice, maxPrice, name, brand);

			if (products.isEmpty()) {
				return Collections.emptyList(); // Trả về danh sách rỗng thay vì null
			}

			Map<Integer, CategoryDTO> categoryMap = new HashMap<>(); // Dùng Map để nhóm theo category_id

			for (Product data : products) {
				int catId = data.getCategory().getId(); // Lấy category_id của sản phẩm

				// Nếu CategoryDTO chưa có trong Map, thì tạo mới
				CategoryDTO categoryDTO = categoryMap.getOrDefault(catId, new CategoryDTO());
				categoryDTO.setName(data.getCategory().getName());
				categoryDTO.setId(data.getCategory().getId()); // Nếu danh sách sản phẩm chưa có, tạo mới
				if (categoryDTO.getProductDTOs() == null) {
					categoryDTO.setProductDTOs(new ArrayList<>());
				}

				// Chuyển đổi Product -> ProductDTO
				ProductDTO productDTO = new ProductDTO();
				productDTO.setId(data.getId());
				productDTO.setImage(data.getImage());
				productDTO.setName(data.getName());
				productDTO.setPrice(data.getPrice());
				productDTO.setDescription(data.getDescription());
				productDTO.setQuantity(data.getQuantity());
				productDTO.setPromo(data.getPromo());
				productDTO.setStatus(data.getStatus());
				productDTO.setBrand(data.getBrand());

				// Thêm sản phẩm vào danh sách của CategoryDTO
				categoryDTO.getProductDTOs().add(productDTO);

				// Thêm vào Map nếu chưa có
				categoryMap.put(catId, categoryDTO);
			}

			// Trả về danh sách các CategoryDTO
			return new ArrayList<>(categoryMap.values());

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return Collections.emptyList(); // Tránh return null
		}
	}

	@Override
	public boolean addCategory(String name) {
		try {
			if (categoryRepository.findByName(name).isPresent()) {
				return false;
			}

			Category category = new Category();
			category.setName(name);
			categoryRepository.save(category);
			return true;

		} catch (Exception e) {
			throw new RuntimeException("Không thể thêm sản phẩm", e);
		}
	}

}
