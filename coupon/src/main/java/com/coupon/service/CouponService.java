package com.coupon.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coupon.model.Coupon;


@Service
public interface CouponService {
	Coupon save(Coupon coupon);
	List<Coupon> findAll();
	Optional<Coupon> findById(int id);
	void deleteById(int id);
}
