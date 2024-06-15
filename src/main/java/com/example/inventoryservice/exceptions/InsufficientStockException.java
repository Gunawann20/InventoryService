package com.example.inventoryservice.exceptions;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message){
        super(message);
    }
}
