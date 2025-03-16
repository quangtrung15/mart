package com.mart.entity;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "promo")
	private int promo;

	@Column(name = "status")
	private String status;

	@Column(name = "brand")
	private String brand;

	@OneToMany(mappedBy = "product")
	private List<RatingProduct> ratingProducts;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<CartDetail> cartDetails;

	@ManyToOne()
	@JoinColumn(name = "category_id")
	private Category category;

}
