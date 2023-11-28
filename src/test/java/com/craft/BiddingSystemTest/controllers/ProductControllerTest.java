package com.craft.BiddingSystemTest.controllers;

import com.craft.biddingSystem.controllers.ProductController;
import com.craft.biddingSystem.models.Product;
import com.craft.biddingSystem.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetProductDetailsSuccess() {
        String category = "Electronics";
        Product expectedProduct = new Product();
        when(productRepository.findByCategory(any())).thenReturn(List.of(expectedProduct));
        ResponseEntity<List<Product>> responseEntity = productController.getProductDetails(category);
        assertEquals(expectedProduct, responseEntity.getBody().get(0));
    }

    @Test
    public void testGetProductDetailsNotFound() {
        String category = "NonexistentCategory";
        when(productRepository.findByCategory(category)).thenReturn(List.of());
        ResponseEntity<List<Product>> responseEntity = productController.getProductDetails(category);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
