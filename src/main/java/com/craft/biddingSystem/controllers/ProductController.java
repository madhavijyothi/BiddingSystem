package com.craft.biddingSystem.controllers;

import com.craft.biddingSystem.repository.ProductRepository;
import com.craft.biddingSystem.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductDetails(@PathVariable String category) {
       List<Product> product = productRepository.findByCategory(category);
        if (product.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(product);
        }
    }
}

