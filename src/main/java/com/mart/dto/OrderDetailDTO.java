package com.mart.dto;

import com.mart.entity.OrderDetail;

import lombok.Data;

@Data
public class OrderDetailDTO {

	private int quantity;
	private ProductDTO productDTO;

	public OrderDetailDTO(OrderDetail orderDetail) {
		this.quantity = orderDetail.getQuantity();
		this.productDTO = new ProductDTO(orderDetail.getProduct());
	}

	public OrderDetailDTO() {
		super();
	}

}
