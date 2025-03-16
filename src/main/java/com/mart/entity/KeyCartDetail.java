package com.mart.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KeyCartDetail implements Serializable {

	@Column(name = "cart_id")
	private long cartId;

	@Column(name = "product_id")
	private long productId;

}
