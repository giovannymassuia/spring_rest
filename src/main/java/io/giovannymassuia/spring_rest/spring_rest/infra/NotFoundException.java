package io.giovannymassuia.spring_rest.spring_rest.infra;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
