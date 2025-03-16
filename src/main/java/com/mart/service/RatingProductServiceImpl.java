package com.mart.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mart.dto.RatingProductDTO;
import com.mart.entity.KeyRatingProduct;
import com.mart.entity.Product;
import com.mart.entity.RatingProduct;
import com.mart.entity.User;
import com.mart.repository.ProductRepository;
import com.mart.repository.RatingProductRepository;
import com.mart.repository.UserRepository;

@Service
public class RatingProductServiceImpl implements RatingProductService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	RatingProductRepository ratingProductRepository;

	@Override
	public RatingProductDTO ratingProduct(long userId, long productId, int ratePoint, String comment) {

		try {
			// Kiểm tra User có tồn tại không
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			// Kiểm tra Product có tồn tại không
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found!"));

			// Tạo KeyRatingProduct
			KeyRatingProduct keyRatingProduct = new KeyRatingProduct();
			keyRatingProduct.setUserId(userId); // Set userId vào KeyRatingProduct
			keyRatingProduct.setProductId(productId); // Set productId vào KeyRatingProduct

			// Tạo RatingProduct và gán KeyRatingProduct
			RatingProduct ratingProduct = new RatingProduct();
			ratingProduct.setKeyRatingProduct(keyRatingProduct); // Gán KeyRatingProduct vào RatingProduct
			ratingProduct.setUser(user);
			ratingProduct.setProduct(product);
			ratingProduct.setRatePoint(ratePoint);
			ratingProduct.setComment(comment);
			ratingProductRepository.save(ratingProduct);
			return new RatingProductDTO(ratingProduct);

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi đánh giá sản phẩm: " + e.getMessage(), e);
		}

	}

}
