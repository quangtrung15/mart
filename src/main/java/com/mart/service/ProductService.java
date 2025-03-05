package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mart.dto.ProductDTO;

@Service
public interface ProductService {

	// Hiển thị sản phẩm theo trạng thái.
	List<ProductDTO> getProductsByStatus(String status);

	boolean addProduct(String name, MultipartFile file, String description, double price, int quantity, int promo,
			String status, String brand, int categoryId);

}
