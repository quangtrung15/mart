package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.CartDTO;
import com.mart.entity.Cart;
import com.mart.entity.CartDetail;

@Service
public interface CartService {

	Cart createCart(int userId, String createdDate, List<CartDetail> cartDetails);

	Cart updateCart(int userId, int cartId, String updatedDate, List<CartDetail> cartDetails);

	boolean deleteCart(int cartId);

	List<CartDTO> getCartByUserId(int userId);

	CartDTO getCartById(int cartId);

}
