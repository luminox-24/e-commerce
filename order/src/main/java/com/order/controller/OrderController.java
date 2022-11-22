package com.order.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
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

import com.order.model.Order;
import com.order.serviceimpl.OrderServiceImpl;



@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome")
public class OrderController {

	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@CrossOrigin("*")
	@PostMapping("/orders")
	public ResponseEntity<?> save(@RequestBody Order order){
		try {
			Order savedOrder=orderServiceImpl.save(order);
			return new ResponseEntity<Order>(savedOrder,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			HashMap<String,String> map=new HashMap<>();
			map.put("error","order already exists");
			return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/orders")
	public ResponseEntity<List<Order>> findAll(){
		try {
			List<Order> orderList=orderServiceImpl.findAll();
			if(orderList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(orderList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> findById(@PathVariable("id")int id){
		Optional<Order> orderData=orderServiceImpl.findById(id);
		if(orderData.isPresent()) {
			return new ResponseEntity<>(orderData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/orders/date/{orderDate}")//date fomrat:yyyy-mm-dd
	public ResponseEntity<List<Order>> findByDate(@PathVariable("orderDate") Date date){
		try {
			List<Order> orderList=orderServiceImpl.findByOrderDate(date);
			if(orderList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(orderList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			orderServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> update(@PathVariable int id,@RequestBody Order order){
		try {
			Optional<Order> orderData= orderServiceImpl.findById(id);
			if(orderData.isPresent()) {
				Order savedOrder=orderData.get();
				savedOrder.setUser(order.getUser());
				savedOrder.setShippingDetails(order.getShippingDetails());
				savedOrder.setOrderTotal(order.getOrderTotal());
				savedOrder.setProducts(order.getProducts());
				savedOrder.setCoupon(order.getCoupon());
				Order updatedOrder = orderServiceImpl.save(savedOrder);
				return new ResponseEntity<Order>(updatedOrder,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
