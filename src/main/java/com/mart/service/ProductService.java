package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mart.dto.ProductDTO;
import com.mart.entity.Product;

@Service
public interface ProductService {

	// Display products by status.
	List<ProductDTO> getProductsByStatus(String status);

	// Add product.
	ProductDTO addProduct(String name, MultipartFile file, String description, double price, int quantity, int promo,
			String status, String brand, long categoryId);

	// Update product.
	ProductDTO updateProduct(long productId, String name, MultipartFile file, String description, double price,
			int quantity, int promo, String status, String brand, long categoryId);

	// Display products by id.
	ProductDTO findById(long productId);

	// Delete product by id.
	boolean deleteById(long productId);

	// Display product list.
	List<ProductDTO> getProduct();

}
