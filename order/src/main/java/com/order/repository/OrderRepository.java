package com.order.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	public List<Order> findByOrderDate(Date date);
}
