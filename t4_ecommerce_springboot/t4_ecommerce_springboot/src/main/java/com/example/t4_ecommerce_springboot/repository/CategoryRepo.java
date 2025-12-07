package com.example.t4_ecommerce_springboot.repository;

import com.example.t4_ecommerce_springboot.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Import required annotations to make use of the Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
	Optional<Category> findByCategoryName(String category);
}