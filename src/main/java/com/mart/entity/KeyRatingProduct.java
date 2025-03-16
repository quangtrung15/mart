package com.mart.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KeyRatingProduct implements Serializable {

	@Column(name = "user_id")
	private long userId;

	@Column(name = "product_id")
	private long productId;

}
