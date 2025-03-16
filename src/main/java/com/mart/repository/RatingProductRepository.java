package com.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mart.entity.KeyRatingProduct;
import com.mart.entity.RatingProduct;

@Repository
public interface RatingProductRepository extends JpaRepository<RatingProduct, KeyRatingProduct> {

}
