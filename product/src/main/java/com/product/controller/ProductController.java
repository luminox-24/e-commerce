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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.helper.Helper;
import com.product.model.Category;
import com.product.model.Product;
import com.product.serviceimpl.CategoryServiceImpl;
import com.product.serviceimpl.ProductServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/shopforhome/")
public class ProductController {
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;


	@CrossOrigin("*")
	@PostMapping("/products/{categoryId}")
	public ResponseEntity<?> save(@PathVariable("categoryId") int id,@RequestBody Product product){
		try {
			Optional<Category> categoryData=categoryServiceImpl.findById(id);
			if(categoryData.isPresent()) {
				product.setCategory(categoryData.get());
				try {
					Product savedProduct=productServiceImpl.save(product);
					return new ResponseEntity<Product>(savedProduct,HttpStatus.CREATED);
				}catch (DataIntegrityViolationException e) {
					System.out.println(e);
					HashMap<String,String> map=new HashMap<>();
					map.put("error","Product already exists");
					return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.CONFLICT);
				}	
			}else {
				return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
			}
		} 
		catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	 @PostMapping("/products/upload")
	 public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
	     if (Helper.checkExcelFormat(file)) {//if true
	        this.productServiceImpl.saveFile(file);
	        return new ResponseEntity<>(HttpStatus.OK);
	     }
	     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	 }
	
	 @CrossOrigin("*")
	@GetMapping("/products")
	public ResponseEntity<List<Product>> findAll(){
		try {
			List<Product> productList=productServiceImpl.findAll();
			if(productList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(productList,HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> findById(@PathVariable("id")int id){
		Optional<Product> productData=productServiceImpl.findById(id);
		if(productData.isPresent()) {
			return new ResponseEntity<>(productData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin("*")
	@GetMapping("/products/category/{CategoryId}")
	public ResponseEntity<List<Product>> findByCategory(@PathVariable("CategoryId") int id){
		try {
			Optional<Category> categoryData=categoryServiceImpl.findById(id);
			if(categoryData.isPresent()) {
				List<Product> productList=productServiceImpl.findByCategory(categoryData.get());
				if(productList.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}else {
					return new ResponseEntity<>(productList,HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> removeById(@PathVariable("id")int id){
		try {
			productServiceImpl.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin("*")
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> update(@PathVariable int id,@RequestBody Product product){
		try {
			Optional<Product> productData= productServiceImpl.findById(id);
			if(productData.isPresent()) {
				Product savedProduct=productData.get();
				savedProduct.setName(product.getName());
				savedProduct.setDescription(product.getDescription());
				savedProduct.setPrice(product.getPrice());
				savedProduct.setImage(product.getImage());
				savedProduct.setStock(product.getStock());
				Product updatedProduct = productServiceImpl.save(savedProduct);
				return new ResponseEntity<Product>(updatedProduct,HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
