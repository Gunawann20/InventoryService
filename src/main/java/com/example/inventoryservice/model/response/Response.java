package com.example.inventoryservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response<T, D> {

    private String status;
    private T errors;
    private D data;
}
