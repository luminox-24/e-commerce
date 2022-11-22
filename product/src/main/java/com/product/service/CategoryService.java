package com.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.product.model.Category;

@Service
public interface CategoryService {
	Category save(Category category);
	List<Category> findAll();
	Optional<Category> findById(int id);
	void deleteById(int id);
}
