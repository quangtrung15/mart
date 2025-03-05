package com.mart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;
import java.text.ParseException;

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
import com.mart.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartDetailRepository cartDetailRepository;

	@Transactional
	@Override
	public Cart createCart(int userId, String createdDate, List<CartDetail> cartDetails) {

		// Kiểm tra User có tồn tại không
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

			// Chuyển đổi createdDate từ String sang Date
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date created_date = simpleDateFormat.parse(createdDate);
			cart.setCreatedDate(created_date);

			// Lưu Cart trước để có ID
			cart = cartRepository.save(cart);

			// 🔹 Lấy ID của Cart vừa lưu
			int newCartId = cart.getId();
			System.out.println("Cart vừa được lưu có ID: " + newCartId);

			// Tính tổng giá trị hóa đơn
			double totalPrice = 0;
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

				cartDetailRepository.save(cartDetail);
			}

			// Cập nhật tổng giá trị hóa đơn
			cart.setPriceTotal(totalPrice);
			cartRepository.save(cart);
			return cart;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tạo giỏ hàng: " + e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public Cart updateCart(int userId, int cartId, String updatedDate, List<CartDetail> cartDetails) {

		// Kiểm tra User có tồn tại không
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

		// Kiểm tra Cart có tồn tại không
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found!"));

		try {
			// Chuyển đổi updatedDate từ String sang Date
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updated_date = simpleDateFormat.parse(updatedDate);
			cart.setUpdatedDate(updated_date);

			// Xóa chi tiết giỏ hàng cũ trước khi cập nhật
			cartDetailRepository.deleteByCartId(cartId);

			// Tính tổng giá trị hóa đơn mới
			double totalPrice = 0;
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

				cartDetailRepository.save(cartDetail);
			}

			// Cập nhật tổng giá trị hóa đơn
			cart.setPriceTotal(totalPrice);
			return cartRepository.save(cart); // Trả về Cart sau khi cập nhật

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi cập nhật giỏ hàng: " + e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public boolean deleteCart(int cartId) {
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
	public List<CartDTO> getCartByUserId(int userId) {

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

					ProductDTO productDTO = new ProductDTO();
					productDTO.setId(data1.getProduct().getId());
					productDTO.setName(data1.getProduct().getName());
					productDTO.setImage(data1.getProduct().getImage());
					productDTO.setDescription(data1.getProduct().getDescription());
					productDTO.setBrand(data1.getProduct().getBrand());
					productDTO.setPrice(data1.getProduct().getPrice());
					productDTO.setStatus(data1.getProduct().getStatus());
					productDTO.setQuantity(data1.getProduct().getQuantity());
					productDTO.setPromo(data1.getProduct().getPromo());

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
	public CartDTO getCartById(int cartId) {

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

				ProductDTO productDTO = new ProductDTO();
				productDTO.setId(data.getProduct().getId());
				productDTO.setName(data.getProduct().getName());
				productDTO.setImage(data.getProduct().getImage());
				productDTO.setDescription(data.getProduct().getDescription());
				productDTO.setBrand(data.getProduct().getBrand());
				productDTO.setPrice(data.getProduct().getPrice());
				productDTO.setStatus(data.getProduct().getStatus());
				productDTO.setQuantity(data.getProduct().getQuantity());
				productDTO.setPromo(data.getProduct().getPromo());

				cartDetailDTO.setProductDTO(productDTO);
				cartDetailDTOs.add(cartDetailDTO);
			}

			cartDTO.setCartDetailDTOs(cartDetailDTOs);
			return cartDTO;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi hiển thị sản phẩm thêm vào giỏ hàng " + e.getMessage(), e);
		}

	}

}
