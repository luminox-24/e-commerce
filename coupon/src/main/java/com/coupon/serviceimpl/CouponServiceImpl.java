package com.coupon.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupon.model.Coupon;
import com.coupon.repository.CouponRepository;
import com.coupon.service.CouponService;
@Service
public class CouponServiceImpl implements CouponService{

	@Autowired
	private CouponRepository couponRepository;
	
	@Override
	public Coupon save(Coupon coupon) {
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAll() {
		// TODO Auto-generated method stub
		return couponRepository.findAll();
	}

	@Override
	public Optional<Coupon> findById(int id) {
		// TODO Auto-generated method stub
		return couponRepository.findById(id);
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		couponRepository.deleteById(id);
	}

}
