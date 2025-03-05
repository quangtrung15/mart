package com.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mart.entity.KeyOrderDetail;
import com.mart.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, KeyOrderDetail>{

	OrderDetail findByOrderIdAndProductId(int orderId, int productId);
	
	@Transactional
	void deleteByOrderId(int orderId);
	
}
