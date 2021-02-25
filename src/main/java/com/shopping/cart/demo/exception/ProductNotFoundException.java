package com.shopping.cart.demo.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) { super(message);}

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
