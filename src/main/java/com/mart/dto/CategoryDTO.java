package com.mart.dto;

import java.util.List;

import com.mart.entity.Category;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CategoryDTO {

	private long id;
	private String name;
	private List<ProductDTO> productDTOs;

	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
	
	public CategoryDTO(long id, String name) {
	    this.id = id;
	    this.name = name;
	}

	public static CategoryDTO toBasicCategoryDTO(Category category) {
		return new CategoryDTO(category.getId(), category.getName());
	}

}
