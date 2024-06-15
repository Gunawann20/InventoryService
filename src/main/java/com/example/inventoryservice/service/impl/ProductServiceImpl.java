package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.exceptions.InsufficientStockException;
import com.example.inventoryservice.exceptions.ProductNotFoundException;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.dtos.ProductDto;
import com.example.inventoryservice.repository.ProductRepository;
import com.example.inventoryservice.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product save(ProductDto product) {

        Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setQuantity(product.getQuantity());

        return productRepository.save(newProduct);
    }

    @Transactional
    @Override
    public Product updateQuantity(Long id, Integer quantity) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findByIdForUpdate(id);
        if (product.isPresent()){
            Product updatedProduct = product.get();
            updatedProduct.setQuantity(quantity);
            return productRepository.save(updatedProduct);
        }

        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public Integer checkAvailability(Long id) throws ProductNotFoundException {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()){
            return byId.get().getQuantity();
        }

        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public List<Product> listProduct() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product orderProduct(Long id, Integer quantity) throws ProductNotFoundException, InsufficientStockException {
        Optional<Product> product = productRepository.findByIdForUpdate(id);
        if (product.isPresent()){
            Product updateProduct = product.get();
            if (updateProduct.getQuantity() < quantity){
                throw new InsufficientStockException("Insufficient stock for product with id: " + id);
            }

            updateProduct.setQuantity(updateProduct.getQuantity() - quantity);
            return productRepository.save(updateProduct);
        }

        throw new ProductNotFoundException("Product not found");
    }
}
