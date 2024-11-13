package ru.romashkaco.myproducts.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long productId) {
        super("Resource with id: " + productId + " not found!");
    }
}
