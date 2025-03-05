package com.mart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mart.entity.CartDetail;
import com.mart.entity.KeyCartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, KeyCartDetail> {

	CartDetail findByCartIdAndProductId(int cartId, int productId);

	@Transactional
	default void deleteByCartId(int cartId) {
		List<CartDetail> cartDetails = findAll().stream().filter(cd -> cd.getKeyCartDetail().getCartId() == cartId)
				.toList();
		deleteAll(cartDetails);
	}

	List<CartDetail> findByCartId(int cartId);

}
