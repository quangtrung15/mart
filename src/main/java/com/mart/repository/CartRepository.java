package com.mart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	@Transactional
	void deleteById(long cartId);

	List<Cart> findByUserId(long userId);

	Optional<Cart> findById(long cartId);
}
