package com.shipping.controller;

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
import org.springframework.web.client.RestTemplate;

import com.shipping.model.Shipping;
import com.shipping.model.User;
import com.shipping.serviceimpl.ShippingServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/")
public class ShippingController {
	
	@Autowired
	private ShippingServiceImpl shippingServiceImpl;

	@Autowired
	RestTemplate restTemplate;
	
	@CrossOrigin("*")
	@PostMapping("/shipping/{userId}")
	public ResponseEntity<?> save(@PathVariable("userId") int id,@RequestBody Shipping shipping){
		try {
			User userData=restTemplate.getForObject("http://user-service/shopforhome/users/"+id, User.class);
			shipping.setUser(userData);
			Shipping savedShipping=shippingServiceImpl.save(shipping);
			return new ResponseEntity<Shipping>(savedShipping,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			System.out.println(e);
			HashMap<String,String> map=new HashMap<>();
			map.put("error","Shipping already exists");
			return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/shipping")
	public ResponseEntity<List<Shipping>> findAll(){
		try {
			List<Shipping> shippingList=shippingServiceImpl.findAll();
			if(shippingList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(shippingList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/shipping/user/{userId}")//will fetch all the shipping details mapped to that particular user
	public ResponseEntity<List<Shipping>> findByUser(@PathVariable("userId") int id){
		try {
			User userData=restTemplate.getForObject("http://user-service/shopforhome/users/"+id, User.class);
			List<Shipping> shippingList=shippingServiceImpl.findByUser(userData);
			if(shippingList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(shippingList,HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/shipping/{id}")
	public ResponseEntity<Shipping> findById(@PathVariable("id")int id){
		Optional<Shipping> shippingData=shippingServiceImpl.findById(id);
		if(shippingData.isPresent()) {
			return new ResponseEntity<>(shippingData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/shipping/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			shippingServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/shipping/{id}")
	public ResponseEntity<Shipping> update(@PathVariable int id,@RequestBody Shipping shipping){
		try {
			Optional<Shipping> shippingData= shippingServiceImpl.findById(id);
			if(shippingData.isPresent()) {
				Shipping savedShipping=shippingData.get();
				savedShipping.setUser(shipping.getUser());
				savedShipping.setName(shipping.getName());
				savedShipping.setPhone(shipping.getPhone());
				savedShipping.setAddress(shipping.getAddress());
				savedShipping.setCity(shipping.getCity());
				savedShipping.setPincode(shipping.getPincode());
				Shipping updatedShipping = shippingServiceImpl.save(savedShipping);
				return new ResponseEntity<Shipping>(updatedShipping,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
