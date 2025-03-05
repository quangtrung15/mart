package com.mart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Transactional
	void deleteById(int cartId);

	List<Cart> findByUserId(int userId);
	
	

}
