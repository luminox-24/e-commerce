package com.coupon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupon.model.Coupon;
import com.coupon.serviceimpl.CouponServiceImpl;



@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/")
public class CouponController {
	@Autowired
	private CouponServiceImpl couponServiceImpl;
	
	@CrossOrigin("*")
	@PostMapping("/coupons")
	public ResponseEntity<?> save(@RequestBody Coupon coupon){
		try {
			Coupon savedCoupon=couponServiceImpl.save(coupon);
			return new ResponseEntity<Coupon>(savedCoupon,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			System.out.println(e);
			HashMap<String,String> map=new HashMap<>();
			map.put("error","Coupon already exists");
			return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/coupons")
	public ResponseEntity<List<Coupon>> findAll(){
		try {
			List<Coupon> couponList=couponServiceImpl.findAll();
			if(couponList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(couponList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/coupons/{id}")
	public ResponseEntity<Coupon> findById(@PathVariable("id")int id){
		Optional<Coupon> couponData=couponServiceImpl.findById(id);
		if(couponData.isPresent()) {
			return new ResponseEntity<>(couponData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/coupons/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			couponServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/coupons/{id}")
	public ResponseEntity<Coupon> update(@PathVariable int id,@RequestBody Coupon coupon){
		try {
			Optional<Coupon> couponData= couponServiceImpl.findById(id);
			if(couponData.isPresent()) {
				Coupon savedCoupon=couponData.get();
				savedCoupon.setName(coupon.getName());
				savedCoupon.setDiscount(coupon.getDiscount());
				Coupon updatedCoupon = couponServiceImpl.save(savedCoupon);
				return new ResponseEntity<Coupon>(updatedCoupon,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
