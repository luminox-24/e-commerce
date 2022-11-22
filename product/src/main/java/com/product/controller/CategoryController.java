package com.product.controller;

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

import com.product.model.Category;
import com.product.serviceimpl.CategoryServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/")
public class CategoryController {
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	@CrossOrigin("*")
	@PostMapping("/category")
	public ResponseEntity<?> save(@RequestBody Category category){
		try {
			Category savedCategory=categoryServiceImpl.save(category);
			return new ResponseEntity<Category>(savedCategory,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			System.out.println(e);
			HashMap<String,String> map=new HashMap<>();
			map.put("error","Category already exists");
			return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@CrossOrigin("*")
	@GetMapping("/category")
	public ResponseEntity<List<Category>> findAll(){
		try {
			List<Category> categoryList=categoryServiceImpl.findAll();
			if(categoryList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(categoryList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/categorys/{id}")
	public ResponseEntity<Category> findById(@PathVariable("id")int id){
		Optional<Category> categoryData=categoryServiceImpl.findById(id);
		if(categoryData.isPresent()) {
			return new ResponseEntity<>(categoryData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/categorys/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			categoryServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/categorys/{id}")
	public ResponseEntity<Category> update(@PathVariable int id,@RequestBody Category category){
		try {
			Optional<Category> categoryData= categoryServiceImpl.findById(id);
			if(categoryData.isPresent()) {
				Category savedCategory=categoryData.get();
				savedCategory.setName(category.getName());
				Category updatedCategory = categoryServiceImpl.save(savedCategory);
				return new ResponseEntity<Category>(updatedCategory,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
