package com.mart.dto;

import com.mart.entity.OrderDetail;

public class OrderDetailDTO {
	
	private int quantity;
	private ProductDTO productDTO;
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.quantity = orderDetail.getQuantity();
        this.productDTO = new ProductDTO(orderDetail.getProduct());
    }

	public OrderDetailDTO() {
		super();
	}
	
	
	
	
	
}
