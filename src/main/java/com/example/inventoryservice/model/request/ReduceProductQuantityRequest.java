package com.example.inventoryservice.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReduceProductQuantityRequest {

    @NotNull(message = "Id cannot be null")
    private Long id;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be equal to or greater than 1")
    private Integer quantity;
}
