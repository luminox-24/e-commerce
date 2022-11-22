package com.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.user.model.Product;
import com.user.model.User;
import com.user.serviceimpl.UserServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/user/")
public class WishlistController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	RestTemplate restTemplate;
	
	@CrossOrigin("*")
	@GetMapping("{id}/wishlist")
	public ResponseEntity<List> findWishlistById(@PathVariable("id") int id){
		Optional<User> userData=userServiceImpl.findById(id);
		if(userData.isPresent()) {
			return new ResponseEntity<>(userData.get().getWishlist(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@PutMapping("{id}/wishlist/{productId}")
	public ResponseEntity<User> addToWishlist(@PathVariable int id,@PathVariable("productId") int productId){
		try {
			Optional<User> userData= userServiceImpl.findById(id);
			Product product=restTemplate.getForObject("http://product-service/shopforhome/products/"+productId,Product.class);
			if(userData.isPresent()) {
				User savedUser=userData.get();
				savedUser.getWishlist().add(product);
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
	@DeleteMapping("{id}/wishlist/{productId}")
	public ResponseEntity<?> removeFromWishlist(@PathVariable("id") int id,@PathVariable("productId") int productId){
		Optional<User> userData=userServiceImpl.findById(id);
		try {
			if(userData.isPresent()) {			
				Product productTobeRomoved=userData.get().getWishlist().stream().filter(p->p.getId()==productId).findFirst().get();
				userData.get().getWishlist().remove(productTobeRomoved);
				User updatedUser=userServiceImpl.save(userData.get());
				return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
			}
			return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
