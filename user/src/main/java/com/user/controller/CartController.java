package com.user.controller;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.user.model.Product;
import com.user.model.Shipping;
import com.user.model.User;
import com.user.model.Coupon;
import com.user.model.Order;
import com.user.serviceimpl.UserServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/user/")
public class CartController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	RestTemplate restTemplate;
	
	@CrossOrigin("*")
	@GetMapping("{id}/cart")
	public ResponseEntity<List> findCartById(@PathVariable("id") int id){
		Optional<User> userData=userServiceImpl.findById(id);
		if(userData.isPresent()) {
			return new ResponseEntity<>(userData.get().getCart(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@PutMapping("{id}/cart/{productId}")
	public ResponseEntity<User> addToCart(@PathVariable int id,@PathVariable("productId") int productId){
		try {
			Optional<User> userData= userServiceImpl.findById(id);
			Product product=restTemplate.getForObject("http://product-service/shopforhome/products/"+productId,Product.class);
			if(userData.isPresent()) {
				User savedUser=userData.get();
				savedUser.getCart().add(product);
				User updatedUser = userServiceImpl.save(savedUser);
				return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	@CrossOrigin("*")
	@DeleteMapping("{id}/cart/{productId}")
	public ResponseEntity<?> removeFromCart(@PathVariable("id") int id,@PathVariable("productId") int productId){
		Optional<User> userData=userServiceImpl.findById(id);
		try {
			if(userData.isPresent()) {			
				Product productTobeRomoved=userData.get().getCart().stream().filter(p->p.getId()==productId).findFirst().get();
				userData.get().getCart().remove(productTobeRomoved);
				User updatedUser=userServiceImpl.save(userData.get());
				return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
			}
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	@CrossOrigin("*")
	@PostMapping("{id}/cart/checkout/{shippingId}/{couponId}")
	public ResponseEntity<Order> createOrder(@PathVariable("id") int id,@PathVariable("shippingId") int shippingId,@PathVariable("couponId") int couponId){
		Shipping shipping=null;
		Coupon coupon=null;
		try {
			coupon=restTemplate.getForObject("http://coupon-service/shopforhome/coupons/"+couponId, Coupon.class);
			shipping=restTemplate.getForObject("http://shipping-service/shopforhome/shipping/"+shippingId, Shipping.class);
			if(shipping==null || coupon==null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Optional<User> userData=userServiceImpl.findById(id);
		try {
			if(userData.isPresent()) {
				User savedUser=userData.get();
				Order order=new Order();
				order.setShippingDetails(shipping);
				order.setUser(savedUser);
				order.setProducts(savedUser.getCart());
				double sum=0.0;
				for (Product p:order.getProducts()) {
					sum+=p.getPrice();
				}
				int discount=coupon.getDiscount();
				System.out.println(((100-discount)/100));
				sum*=((100-discount)/100.0);
				order.setOrderTotal(sum);
				order.setCoupon(coupon);
				Order savedOrder=restTemplate.postForObject("http://order-service/shopforhome/orders", order,Order.class);
				order.getProducts().forEach(p->{
					p.setStock(p.getStock()-1);
					System.out.println(p.getId());
					restTemplate.put("http://product-service/shopforhome/products/"+p.getId(),p);
				});
				savedUser.getCart().clear();
				userServiceImpl.save(savedUser);
				return new ResponseEntity<>(savedOrder,HttpStatus.CREATED);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
