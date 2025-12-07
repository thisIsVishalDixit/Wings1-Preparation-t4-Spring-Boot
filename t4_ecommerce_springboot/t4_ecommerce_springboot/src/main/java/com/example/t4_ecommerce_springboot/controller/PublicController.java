package com.example.t4_ecommerce_springboot.controller;

import com.example.t4_ecommerce_springboot.models.Product;
import com.example.t4_ecommerce_springboot.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Import required Annotations and implement the  business logics
@RestController
@RequestMapping("/api/public")
public class PublicController {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	UserInfoRepository userRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/product/search")
	public ResponseEntity< List<Product>> getProducts(@RequestParam String keyword) {
		List<Product> l = productRepo.findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(keyword, keyword);
		return ResponseEntity.ok().body(l);
	}
}