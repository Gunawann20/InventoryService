package com.example.inventoryservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.inventoryservice.model.Product;
import com.example.inventoryservice.model.dtos.ProductDto;
import com.example.inventoryservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void addProduct() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setName("TWS");
        productDto.setPrice(122000.0);
        productDto.setQuantity(20);

        Product product = new Product();
        product.setId(1L);
        product.setName("TWS");
        product.setPrice(122000.0);
        product.setQuantity(20);

        Mockito.when(productService.save(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(post("/api/inventory/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("TWS"))
                .andExpect(jsonPath("$.data.price").value(122000.0))
                .andExpect(jsonPath("$.data.quantity").value(20));
    }

    @Test
    void getAllProducts() throws Exception {

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("TWS");
        product1.setPrice(122000.0);
        product1.setQuantity(20);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("TWS X-2");
        product2.setPrice(144000.0);
        product2.setQuantity(50);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Mockito.when(productService.listProduct()).thenReturn(products);

        mockMvc.perform(get("/api/inventory/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("TWS"))
                .andExpect(jsonPath("$.data[0].price").value(122000.0))
                .andExpect(jsonPath("$.data[0].quantity").value(20))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("TWS X-2"))
                .andExpect(jsonPath("$.data[1].price").value(144000.0))
                .andExpect(jsonPath("$.data[1].quantity").value(50));
    }

    @Test
    void handleValidationExceptions() throws Exception {

        ProductDto productDto = new ProductDto();

        mockMvc.perform(post("/api/inventory/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors.name").value("Name cannot be null"))
                .andExpect(jsonPath("$.errors.quantity").value("Quantity cannot be null"))
                .andExpect(jsonPath("$.errors.price").value("Price cannot be null"));
    }
}