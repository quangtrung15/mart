package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.CartDTO;
import com.mart.entity.Cart;
import com.mart.entity.CartDetail;

@Service
public interface CartService {

	CartDTO createCart(long userId, List<CartDetail> cartDetails);

	CartDTO updateCart(long userId, long cartId, String updatedDate, List<CartDetail> cartDetails);

	boolean deleteCart(long cartId);

	List<CartDTO> getCartByUserId(long userId);

	CartDTO getCartById(long cartId);

}
