package com.mart.dto;

import com.mart.entity.CartDetail;
import com.mart.entity.OrderDetail;

import lombok.Data;

@Data
public class CartDetailDTO {

	private int quantity;
	private ProductDTO productDTO;

	public CartDetailDTO(CartDetail cartDetail) {
		this.quantity = cartDetail.getQuantity();
		this.productDTO = new ProductDTO(cartDetail.getProduct());
	}

	public CartDetailDTO() {
		super();
	}

}
