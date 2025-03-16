package com.mart.dto;

import com.mart.entity.Product;

import lombok.Data;

@Data
public class ProductDTOBuilder {

	private ProductDTO productDTO;

	public ProductDTOBuilder(Product product) {
		productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setImage(product.getImage());
		productDTO.setDescription(product.getDescription());
		productDTO.setPrice(product.getPrice());
		productDTO.setQuantity(product.getQuantity());
		productDTO.setPromo(product.getPromo());
		productDTO.setStatus(product.getStatus());
		productDTO.setBrand(product.getBrand());
		productDTO.setCategoryId(product.getCategory().getId());
	}

//	public ProductDTOBuilder excludeCategory() {
//		productDTO.setCategoryId(null);
//		return this;
//	}

}
