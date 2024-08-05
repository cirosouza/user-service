package br.edu.infnet.user_service.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String detail) {
        super(detail);
    }
}