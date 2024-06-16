package com.example.inventoryservice.controller;

import com.example.inventoryservice.exceptions.InsufficientStockException;
import com.example.inventoryservice.exceptions.ProductNotFoundException;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.dtos.ProductDto;
import com.example.inventoryservice.model.request.OrderProductRequest;
import com.example.inventoryservice.model.request.UpdateProductRequest;
import com.example.inventoryservice.model.response.Response;
import com.example.inventoryservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<Boolean, Product> addProduct(@Valid @RequestBody ProductDto product){
        Product newProduct = productService.save(product);
        return new Response<>(SUCCESS, Boolean.FALSE, newProduct);
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<Object, List<Product>> getAllProducts(){
        List<Product> products = productService.listProduct();
        return new Response<>(SUCCESS, null, products);
    }

    @PostMapping(
            value = "/quantity",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<Object, Product> updateProduct(@Valid @RequestBody UpdateProductRequest request) throws ProductNotFoundException {
        Product product = productService.updateQuantity(request.getId(), request.getQuantity());
        return new Response<>(SUCCESS, null, product);
    }

    @PostMapping(
            value = "/order",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<Object, Product> orderProduct(@Valid @RequestBody OrderProductRequest request) throws InsufficientStockException, ProductNotFoundException {
        Product product = productService.orderProduct(request.getId(), request.getQuantity());
        return new Response<>(SUCCESS, null, product);
    }


    @GetMapping(
            value = "/quantity/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<Object, Map<String, Object>> getQuantity(@PathVariable Long id) throws ProductNotFoundException {
        Integer quantity = productService.checkAvailability(id);
        HashMap<String, Object> data = new HashMap<>();
        data.put("product_id", id);
        data.put("quantity", quantity);
        return new Response<>(SUCCESS, null, data);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Map<String, String>, Product> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new Response<>(ERROR, errors, null);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Response<Map<String, String>, Object> handledProductNotFound(ProductNotFoundException exception){
        HashMap<String, String> error = new HashMap<>();
        error.put("message", exception.getMessage());
        return new Response<>(ERROR, error, null);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public Response<Map<String, String>, Object> handledProductNotFound(InsufficientStockException exception){
        HashMap<String, String> error = new HashMap<>();
        error.put("message", exception.getMessage());
        return new Response<>(ERROR, error, null);
    }
}
