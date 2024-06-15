package com.example.inventoryservice.service;

import com.example.inventoryservice.exceptions.InsufficientStockException;
import com.example.inventoryservice.exceptions.ProductNotFoundException;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.dtos.ProductDto;
import java.util.List;

public interface ProductService {

    Product save(ProductDto product);
    Product updateQuantity(Long id, Integer quantity) throws ProductNotFoundException;
    Integer checkAvailability(Long id) throws ProductNotFoundException;
    List<Product> listProduct();
    Product reduceQuantity(Long id, Integer quantity) throws ProductNotFoundException, InsufficientStockException;
}
