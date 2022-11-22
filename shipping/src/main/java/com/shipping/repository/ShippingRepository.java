package com.shipping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shipping.model.Shipping;
import com.shipping.model.User;
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer>{
	public List<Shipping> findByUser(User user);
}
