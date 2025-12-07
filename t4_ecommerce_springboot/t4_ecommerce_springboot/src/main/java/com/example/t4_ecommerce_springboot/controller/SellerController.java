package com.example.t4_ecommerce_springboot.controller;

import com.example.t4_ecommerce_springboot.models.*;
import com.example.t4_ecommerce_springboot.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

// Import required Annotations and implement the  business logics
@RestController
@RequestMapping("/api/auth/seller")
@PreAuthorize("hasRole('SELLER')")
public class SellerController {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	UserInfoRepository userRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@GetMapping("/hi")
    public String getMethodName() {
        return new String("hi");
    }

	@PostMapping("/product")
	public ResponseEntity<Object> postProduct(Principal principal,@RequestBody Product product) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Category c = categoryRepo.findByCategoryName(product.getCategory().getCategoryName()).orElseGet(()->new Category(product.getCategory().getCategoryName()));
		product.setCategory(c);
		product.setSeller(user.get());
		productRepo.save(product);
		URI url=URI.create("http://localhost/api/auth/seller/product/"+product.getProductId());
		return ResponseEntity.created(url).body(product);

	}

	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts(Principal principal) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		List<Product> p = productRepo.findBySellerUserId(user.get().getUserId());

		return ResponseEntity.ok(p);
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct(Principal principal,@PathVariable  Integer productId) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Optional<Product> p = productRepo.findBySellerUserIdAndProductId(user.get().getUserId(), productId);
		if(p.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(p.get());
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct(Principal principal, @RequestBody Product updatedProduct) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Optional<Product> p = productRepo.findBySellerUserIdAndProductId(user.get().getUserId(), updatedProduct.getProductId());
		if(p.isEmpty()){
			return ResponseEntity.notFound().build();
		}

		Category c = categoryRepo.findByCategoryName(updatedProduct.getCategory().getCategoryName()).orElseGet(()->new Category(updatedProduct.getCategory().getCategoryName()));
		
		p.get().setCategory(c);
		p.get().setSeller(user.get());
		p.get().setPrice(updatedProduct.getPrice());
		p.get().setProductName(updatedProduct.getProductName());
		productRepo.save(p.get());
		return ResponseEntity.ok(p);

		
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct(Principal principal,@PathVariable  Integer productId) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Optional<Product> p = productRepo.findById(productId);
		if(p.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		productRepo.delete(p.get());
		return ResponseEntity.ok().build();
	}

}
