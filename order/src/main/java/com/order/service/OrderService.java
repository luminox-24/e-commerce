package com.order.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.order.model.Order;

@Service
public interface OrderService {
	Order save(Order order);
	List<Order> findAll();
	List<Order> findByOrderDate(Date date);
	Optional<Order> findById(int id);
	void deleteById(int id);
}
