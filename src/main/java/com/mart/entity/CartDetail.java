package com.mart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cart_details")
public class CartDetail {

	@EmbeddedId
	KeyCartDetail keyCartDetail;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "cart_id", insertable = false, updatable = false)
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

}
