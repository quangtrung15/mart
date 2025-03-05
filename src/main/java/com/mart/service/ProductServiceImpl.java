package com.mart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mart.dto.ProductDTO;
import com.mart.entity.Category;
import com.mart.entity.Product;
import com.mart.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	FileService fileService;

	// Hiển thị sản phẩm theo trạng thái.
	@Override
	public List<ProductDTO> getProductsByStatus(String status) {

		try {

			List<Product> products = productRepository.findByStatusContainingIgnoreCase(status);
			List<ProductDTO> productDTOs = new ArrayList<>();

			if (products.isEmpty()) {
				throw new NotFoundException();
			}

			for (Product data : products) {
				ProductDTO productDTO = new ProductDTO();
				productDTO.setId(data.getId());
				productDTO.setName(data.getName());
				productDTO.setImage(data.getImage());
				productDTO.setDescription(data.getDescription());
				productDTO.setBrand(data.getBrand());
				productDTO.setQuantity(data.getQuantity());
				productDTO.setPromo(data.getPromo());
				productDTO.setPrice(data.getPrice());
				productDTO.setStatus(data.getStatus());
				productDTOs.add(productDTO);
			}

			return productDTOs;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm theo thể trạng thái", e);
		}

	}

	@Override
	public boolean addProduct(String name, MultipartFile file, String description, double price, int quantity,
			int promo, String status, String brand, int categoryId) {

		try {

			if (file == null || file.isEmpty()) {
				System.out.println("Error: File is empty");
				return false;
			}

			boolean isSaveFileSuccess = fileService.saveFile(file);
			if (isSaveFileSuccess == false) {
				throw new RuntimeException("Ảnh chưa được lưu thành công!");
			}

			Product product = new Product();
			product.setName(name);
			product.setImage(file.getOriginalFilename());
			product.setDescription(description);
			product.setPrice(price);
			product.setQuantity(quantity);
			product.setPromo(promo);
			product.setStatus(status);
			product.setBrand(brand);

			Category category = new Category();
			category.setId(categoryId);
			product.setCategory(category);

			productRepository.save(product);

			return true;

		} catch (Exception e) {
			throw new RuntimeException("Không thêm được sản phẩm", e);
		}

	}

}
