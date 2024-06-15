package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.exceptions.InsufficientStockException;
import com.example.inventoryservice.exceptions.ProductNotFoundException;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.dtos.ProductDto;
import com.example.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave(){
        ProductDto productDto = new ProductDto();
        productDto.setName("Test product");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test product");
        product.setPrice(100.0);
        product.setQuantity(10);

        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saveProduct = productService.save(productDto);

        assertNotNull(saveProduct);
        assertEquals("Test product", saveProduct.getName());
        assertEquals(100.0, saveProduct.getPrice());
        assertEquals(10, saveProduct.getQuantity());
    }

    @Test
    void testUpdateQuantity() throws ProductNotFoundException {

        Product product = new Product();
        product.setId(1L);
        product.setName("Test product");
        product.setPrice(100.0);
        product.setQuantity(10);

        Mockito.when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));

        Product productUpdate = new Product();
        productUpdate.setId(1L);
        productUpdate.setName("Test product");
        productUpdate.setPrice(100.0);
        productUpdate.setQuantity(20);

        Mockito.when(productRepository.save(product)).thenReturn(productUpdate);

        Product updateProduct = productService.updateQuantity(1L, 20);

        assertNotNull(updateProduct);
        assertEquals("Test product", updateProduct.getName());
        assertEquals(100.0, updateProduct.getPrice());
        assertEquals(20, updateProduct.getQuantity());
    }

    @Test
    void testCheckAvailability() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Integer quantity = null;
        try {
            quantity = productService.checkAvailability(1L);
        } catch (ProductNotFoundException e) {
            fail("ProductNotFoundException was thrown");
        }

        assertNotNull(quantity);
        assertEquals(10, quantity);
    }

    @Test
    void testCheckAvailability_ProductNotFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.checkAvailability(1L);
        });
    }

    @Test
    void testListProduct() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(50.0);
        product1.setQuantity(5);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(150.0);
        product2.setQuantity(15);

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> productList = productService.listProduct();

        assertNotNull(productList);
        assertEquals(2, productList.size());
    }

    @Test
    void testReduceQuantity_InsufficientStock() {
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(3);

        Mockito.when(productRepository.findByIdForUpdate(anyLong())).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> {
            productService.reduceQuantity(1L, 5);
        });

        verify(productRepository, times(1)).findByIdForUpdate(1L);
        verify(productRepository, times(0)).save(any(Product.class));
    }
}