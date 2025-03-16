package com.mart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mart.dto.CartDTO;
import com.mart.dto.CartDetailDTO;
import com.mart.dto.ProductDTO;
import com.mart.entity.Cart;
import com.mart.entity.CartDetail;
import com.mart.entity.KeyCartDetail;
import com.mart.entity.User;
import com.mart.repository.CartDetailRepository;
import com.mart.repository.CartRepository;
import com.mart.repository.ProductRepository;
import com.mart.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartDetailRepository cartDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Override
	@Transactional
	public CartDTO createCart(long userId, List<CartDetail> cartDetails) {

		// Kiểm tra User có tồn tại không.
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

		// Kiểm tra xem danh sách sản phẩm có null không.
		if (cartDetails == null || cartDetails.isEmpty()) {
			throw new RuntimeException("Danh sách sản phẩm không được để trống!");
		}

		try {
			// Tạo Cart mới và gán thông tin User
			Cart cart = new Cart();
			cart.setUser(user);

			// Lưu Cart trước để có ID
			cart = cartRepository.save(cart);

			// Tính tổng giá trị hóa đơn
			double totalPrice = 0;

			List<CartDetail> saveCartDetails = new ArrayList<>();

			for (CartDetail data : cartDetails) {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setQuantity(data.getQuantity());
				cartDetail.setProduct(data.getProduct());
				cartDetail.setCart(cart);

				// Tạo khóa chính tổng hợp KeyCartDetail
				KeyCartDetail keyCartDetail = new KeyCartDetail();
				keyCartDetail.setCartId(cart.getId());
				keyCartDetail.setProductId(data.getProduct().getId());
				cartDetail.setKeyCartDetail(keyCartDetail);

				double priceProduct = data.getProduct().getPrice();
				totalPrice += data.getQuantity() * priceProduct;

				saveCartDetails.add(cartDetail);

				cartDetailRepository.save(cartDetail);
			}

			// Cập nhật tổng giá trị hóa đơn
			cart.setPriceTotal(totalPrice);
			cart.setCartDetails(saveCartDetails);
			cartRepository.save(cart);
			return new CartDTO(cart);

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tạo giỏ hàng: " + e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public boolean deleteCart(long cartId) {
		try {
			// Kiểm tra xem giỏ hàng có tồn tại không
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found!"));

			cartDetailRepository.deleteByCartId(cartId);
			cartRepository.deleteById(cartId);

			return true; // Xóa thành công

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi xóa giỏ hàng: " + e.getMessage(), e);
		}
	}

	@Override
	public List<CartDTO> getCartByUserId(long userId) {

		try {
			List<Cart> carts = cartRepository.findByUserId(userId);

			if (carts.isEmpty()) {
				throw new RuntimeException("Chưa có sản phẩm nào");
			}

			List<CartDTO> cartDTOs = new ArrayList<CartDTO>();

			for (Cart data : carts) {

				CartDTO cartDTO = new CartDTO();
				cartDTO.setId(data.getId());
				cartDTO.setPriceTotal(data.getPriceTotal());
				cartDTO.setCreatedDate(data.getCreatedDate());
				cartDTO.setUpdatedDate(data.getUpdatedDate());

				List<CartDetailDTO> cartDetailDTOs = new ArrayList<CartDetailDTO>();

				for (CartDetail data1 : data.getCartDetails()) {

					CartDetailDTO cartDetailDTO = new CartDetailDTO();
					cartDetailDTO.setQuantity(data1.getQuantity());

					ProductDTO productDTO = new ProductDTO(data1.getProduct());

					cartDetailDTO.setProductDTO(productDTO);
					cartDetailDTOs.add(cartDetailDTO);
				}

				cartDTO.setCartDetailDTOs(cartDetailDTOs);
				cartDTOs.add(cartDTO);
			}

			return cartDTOs;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi hiển thị giỏ hàng theo người dùng " + e.getMessage(), e);
		}

	}

	@Override
	public CartDTO getCartById(long cartId) {

		try {
			// Kiểm tra xem giỏ hàng có tồn tại không
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found!"));

			CartDTO cartDTO = new CartDTO();
			cartDTO.setId(cart.getId());
			cartDTO.setPriceTotal(cart.getPriceTotal());
			cartDTO.setCreatedDate(cart.getCreatedDate());
			cartDTO.setUpdatedDate(cart.getUpdatedDate());

			List<CartDetailDTO> cartDetailDTOs = new ArrayList<CartDetailDTO>();

			for (CartDetail data : cart.getCartDetails()) {
				CartDetailDTO cartDetailDTO = new CartDetailDTO();
				cartDetailDTO.setQuantity(data.getQuantity());

				ProductDTO productDTO = new ProductDTO(data.getProduct());

				cartDetailDTO.setProductDTO(productDTO);
				cartDetailDTOs.add(cartDetailDTO);
			}

			cartDTO.setCartDetailDTOs(cartDetailDTOs);
			return cartDTO;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi hiển thị sản phẩm thêm vào giỏ hàng " + e.getMessage(), e);
		}

	}

	@Override
	public CartDTO updateCart(long userId, long cartId, String updatedDate, List<CartDetail> cartDetails) {

		try {

			Optional<User> userOptional = userRepository.findById(userId);
			Optional<Cart> cartOptional = cartRepository.findById(cartId);
			if (userOptional.isEmpty() && cartOptional.isEmpty()) {
				throw new RuntimeException();
			}

			User user = userOptional.get();
			Cart cart = cartOptional.get();

			cartDetailRepository.deleteByCartId(cart.getId());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updated_date = simpleDateFormat.parse(updatedDate);
			cart.setUpdatedDate(updated_date);

			double priceTotal = 0;

			List<CartDetail> saveCartDetails = new ArrayList<>();

			for (CartDetail data : cartDetails) {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setCart(cart);
				cartDetail.setProduct(data.getProduct());
				cartDetail.setQuantity(data.getQuantity());

				KeyCartDetail keyCartDetail = new KeyCartDetail(cart.getId(), data.getProduct().getId());
				cartDetail.setKeyCartDetail(keyCartDetail);

				double priceProduct = data.getQuantity() * data.getProduct().getPrice();
				priceTotal += priceProduct;

				saveCartDetails.add(cartDetail);

				cartDetailRepository.save(cartDetail);

			}

			cart.setPriceTotal(priceTotal);
			cart.setCartDetails(saveCartDetails);
			cartRepository.save(cart);

			return new CartDTO(cart);

		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

}
