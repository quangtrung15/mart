package com.mart.service;

import org.springframework.stereotype.Service;

@Service
public interface RatingProductService {

	boolean ratingProduct(int userId, int productId, int ratePoint, String comment);

}
