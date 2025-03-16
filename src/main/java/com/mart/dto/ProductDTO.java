package com.mart.dto;

import com.mart.entity.Product;

import lombok.Data;

@Data
public class ProductDTO {

	private long id;
	private String name;
	private String image;
	private String description;
	private double price;
	private int quantity;
	private int promo;
	private String status;
	private String brand;
	private long categoryId;

	public ProductDTO() {
		super();
	}

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.image = product.getImage();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
		this.promo = product.getPromo();
		this.status = product.getStatus();
		this.brand = product.getBrand();
		this.categoryId = product.getCategory().getId();
	}

//	public ProductDTO excludeCategory() {
//		this.setCategory(null);
//		return this;
//	}

//	public static ProductDTO chuyendoi(Product product) {
//		return new ProductDTO(product.getId(), product.getName(), product.getImage(), product.getDescription(),
//				product.getPrice(), product.getQuantity(), product.getPromo(), product.getStatus(), product.getBrand());
//	}

}
