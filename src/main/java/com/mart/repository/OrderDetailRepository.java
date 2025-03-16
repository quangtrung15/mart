package com.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mart.entity.KeyOrderDetail;
import com.mart.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, KeyOrderDetail>{

	OrderDetail findByOrderIdAndProductId(long orderId, long productId);
	
	@Transactional
	void deleteByOrderId(long orderId);
	
}
