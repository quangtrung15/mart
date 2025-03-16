package com.mart.service;

import org.springframework.stereotype.Service;

import com.mart.dto.RatingProductDTO;

@Service
public interface RatingProductService {

	RatingProductDTO ratingProduct(long userId, long productId, int ratePoint, String comment);

}
