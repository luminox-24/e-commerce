package com.shipping.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shipping.model.Shipping;
import com.shipping.model.User;

@Service
public interface ShippingService {
	Shipping save(Shipping shipping);
	List<Shipping> findAll();
	List<Shipping> findByUser(User user);
	Optional<Shipping> findById(int id);
	void deleteById(int id);
}
