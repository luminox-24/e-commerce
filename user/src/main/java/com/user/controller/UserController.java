package com.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.jwtutil.JwtUtil;
import com.user.model.Type;
import com.user.model.User;
import com.user.serviceimpl.UserServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@CrossOrigin("*")
	@PostMapping("/users")
	public ResponseEntity<?> save(@RequestBody User user){
		try {
			User savedUser=userServiceImpl.save(user);
			return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			System.out.println(e);
			HashMap<String,String> map=new HashMap<>();
			map.put("error","User already exists");
			return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/userlogin/{email}/{password}")
	public ResponseEntity<?> loginAuth(@PathVariable("email") String email,@PathVariable("password") String password){
		Optional<User> userData=userServiceImpl.findByEmail(email);
		HashMap<String,String> map=new HashMap<>();
		if(userData.isPresent()) {
			try {
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(userData.get().getEmail(), userData.get().getPassword())
	            );
	        } catch (Exception ex) {
	        	return new ResponseEntity<String>("Invalid password",HttpStatus.NOT_FOUND);
	        }
			map.put("id",""+userData.get().getId());
			map.put("token",jwtUtil.generateToken(userData.get().getEmail()));
	        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/users")
	public ResponseEntity<List<User>> findAll(){
		try {
			List<User> userList=userServiceImpl.findAll();
			if(userList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(userList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/users/{id}")
	public ResponseEntity<User> findById(@PathVariable("id")int id){
		Optional<User> userData=userServiceImpl.findById(id);
		if(userData.isPresent()) {
			return new ResponseEntity<>(userData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			userServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/users/{id}")
	public ResponseEntity<User> update(@PathVariable int id,@RequestBody User user){
		try {
			Optional<User> userData= userServiceImpl.findById(id);
			if(userData.isPresent()) {
				User savedUser=userData.get();
				savedUser.setType(user.getType());
				savedUser.setName(user.getName());
				savedUser.setEmail(user.getEmail());
				savedUser.setPhone(user.getPhone());
				savedUser.setPassword(user.getPassword());
				savedUser.setCart(user.getCart());
				savedUser.setWishlist(user.getWishlist());
				User updatedUser = userServiceImpl.save(savedUser);
				return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
