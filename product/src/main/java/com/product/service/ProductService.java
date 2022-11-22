package com.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.model.Category;
import com.product.model.Product;

@Service
public interface ProductService {
	Product save(Product product);
	void saveFile(MultipartFile file);
	List<Product> findAll();
	List<Product> findByCategory(Category category);
	Optional<Product> findById(int id);
	void deleteById(int id);
}
