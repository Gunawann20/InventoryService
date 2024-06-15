package com.example.inventoryservice.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be equal to or greater than 0")
    private Double price;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be equal to or greater than 0")
    private Integer quantity;
}
