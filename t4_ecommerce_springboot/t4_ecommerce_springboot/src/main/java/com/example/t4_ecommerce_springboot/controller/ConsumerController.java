package com.example.t4_ecommerce_springboot.controller;

import com.example.t4_ecommerce_springboot.config.UserInfoUserDetails;
import com.example.t4_ecommerce_springboot.models.Cart;
import com.example.t4_ecommerce_springboot.models.CartProduct;
import com.example.t4_ecommerce_springboot.models.Product;
import com.example.t4_ecommerce_springboot.models.UserInfo;
import com.example.t4_ecommerce_springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

// Import required Annotations and implement the  business logics
@RestController
@RequestMapping("/api/auth/consumer")
@PreAuthorize("hasRole('CONSUMER')")
public class ConsumerController {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartProductRepo cpRepo;

	@Autowired
	UserInfoRepository userRepo;

	@GetMapping("/cart")
	public ResponseEntity<Object> getCart(Principal principal) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Cart c = cartRepo.findByUserUsername(username).orElseGet(()->{
			Cart ca=new Cart();
			ca.setUser(user.get());
			return ca;
		});
		return ResponseEntity.ok(c);

	}

	@PostMapping("/cart")
	public ResponseEntity<Object> postCart(Principal principal,@RequestBody Product product) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		
		Optional<Product> p = productRepo.findById(product.getProductId());
		if(p.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		Cart c = cartRepo.findByUserUsername(username).orElseGet(()->{
			Cart ca=new Cart();
			ca.setUser(user.get());
			return ca;
		});
		Optional<CartProduct> cp = cpRepo.findByCartUserUserIdAndProductProductId(c.getCartId(), product.getProductId());
		if(cp.isPresent()){
			return ResponseEntity.status(409).build();
		}else{
			CartProduct cp2=new CartProduct();
			cp2.setCart(c);
			cp2.setProduct(p.get());
			cpRepo.save(cp2);
			return ResponseEntity.ok(cp2);
		}
		

	}


	@PutMapping("/cart")
	public ResponseEntity<Object> putCart(Principal principal,@RequestBody  CartProduct cp) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}

		Optional<Product> p = productRepo.findById(cp.getProduct().getProductId());
		if(p.isEmpty()){
			return ResponseEntity.notFound().build();
		}

		Cart c = cartRepo.findByUserUsername(username).orElseGet(()->{
			Cart ca=new Cart();
			ca.setUser(user.get());
			return ca;
		});

		Optional<CartProduct> cartp = cpRepo.findByCartUserUserIdAndProductProductId(c.getUser().getUserId(), p.get().getProductId());
		if(cartp.isPresent()){
			if(cp.getQuantity()==0){
				cpRepo.delete(cartp.get());
			}
			else{
				cartp.get().setQuantity(cp.getQuantity());
				cpRepo.save(cartp.get());
			}
			return ResponseEntity.ok().build();

		}
		else{
			CartProduct cartp2 = new CartProduct();
			cartp2.setProduct(p.get());
			cartp2.setCart(c);
			cartp2.setQuantity(cp.getQuantity());
			cpRepo.save(cartp2);
			return ResponseEntity.ok().body(cartp2);
		}
		
	}


	@DeleteMapping("/cart")
	public ResponseEntity<Object> deleteCart(Principal principal, @RequestBody Product product) {
		String username=principal.getName();
		Optional<UserInfo> user = userRepo.findByUsername(username);
		if(user.isEmpty()){
			return ResponseEntity.badRequest().build();
		}
		Optional<CartProduct> cartp = cpRepo.findByCartUserUserIdAndProductProductId(user.get().getUserId(), product.getProductId());
		if(cartp.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		cpRepo.delete(cartp.get());
		return ResponseEntity.ok().build();
	}

}
