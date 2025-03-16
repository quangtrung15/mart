package com.mart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mart.dto.ProductDTO;
import com.mart.entity.Category;
import com.mart.entity.Product;
import com.mart.repository.CategoryRepository;
import com.mart.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	FileService fileService;

	// Display products by status.
	@Override
	public List<ProductDTO> getProductsByStatus(String status) {

		try {

			List<Product> products = productRepository.findByStatus(status);
			List<ProductDTO> productDTOs = new ArrayList<>();

			if (products.isEmpty()) {
				throw new NotFoundException();
			}

			for (Product data : products) {
				ProductDTO productDTO = new ProductDTO(data);
				productDTOs.add(productDTO);
			}

			return productDTOs;

		} catch (NotFoundException nfe) {
			throw new RuntimeException("Error not found product by status", nfe);
		} catch (Exception e) {
			throw new RuntimeException("Error when getting product list by status", e);
		}

	}

	// Add product.
	@Override
	public ProductDTO addProduct(String name, MultipartFile file, String description, double price, int quantity,
			int promo, String status, String brand, long categoryId) {

		try {

			if (file == null || file.isEmpty()) {
				throw new RuntimeException("Ảnh rỗng!");
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

			Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

			if (categoryOptional.isEmpty()) {
				throw new RuntimeException("Không có thể loại sản phẩm tương ứng");
			}

			Category category = categoryOptional.get();

			product.setCategory(category);
			productRepository.save(product);
			return new ProductDTO(product);

		} catch (Exception e) {
			throw new RuntimeException("Không thêm được sản phẩm", e);
		}

	}

	// Display products by id.
	@Override
	public ProductDTO findById(long productId) {

		try {
			Optional<Product> productOptional = productRepository.findById(productId);

			if (productOptional.isEmpty()) {
				throw new RuntimeException("Không tìm thấy sản phẩm theo id");
			}

			Product product = productOptional.get();

			return new ProductDTO(product);

		} catch (Exception e) {
			throw new RuntimeException("Lỗi không tìm thấy sản phẩm theo id");
		}

	}

	// Update product.
	@Override
	public ProductDTO updateProduct(long productId, String name, MultipartFile file, String description, double price,
			int quantity, int promo, String status, String brand, long categoryId) {

		try {
			Optional<Product> productOptional = productRepository.findById(productId);

			if (productOptional.isEmpty()) {
				throw new RuntimeException("Lỗi không tìm thấy sản phẩm theo id!");
			}

			Product product = productOptional.get();

			Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

			if (categoryOptional.isEmpty()) {
				throw new RuntimeException("Lỗi không tìm thấy thể loại theo id!");
			}

			Category category = categoryOptional.get();

			product.setBrand(brand);
			product.setDescription(description);
			product.setImage(file.getOriginalFilename());
			product.setName(name);
			product.setPrice(price);
			product.setPromo(promo);
			product.setQuantity(quantity);
			product.setStatus(status);
			product.setCategory(category);

			return new ProductDTO(productRepository.save(product));

		} catch (Exception e) {
			throw new RuntimeException("Lỗi cập nhật sản phẩm!");
		}

	}

	// Delete product by id.
	@Override
	public boolean deleteById(long productId) {

		try {
			productRepository.deleteById(productId);
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi xóa sản phẩm!");
		}

	}

	// Display product list.
	@Override
	public List<ProductDTO> getProduct() {

		try {
			List<Product> products = productRepository.findAll();
			List<ProductDTO> productDTOs = new ArrayList<>();
			for (Product data : products) {
				ProductDTO productDTO = new ProductDTO(data);
				productDTOs.add(productDTO);
			}
			return productDTOs;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi hiển thị danh sản phẩm!");
		}

	}

//	@Override
//	public List<ProductDTO> getProduct() {
//
//		try {
//			List<Product> products = productRepository.findAll();
//			List<ProductDTO> productDTOs = new ArrayList<>();
//			for (Product data : products) {
//				productDTOs.add(ProductDTO.chuyendoi(data));
//			}
//			return productDTOs;
//
//		} catch (Exception e) {
//			throw new RuntimeException("Lỗi khi hiển thị danh sản phẩm!");
//		}
//
//	}

}
