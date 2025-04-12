package com.bassi.tmapp.web.rest.errors;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InternalServerAlertException extends RuntimeException {

    public InternalServerAlertException(String message) {
        super(message);
    }
}
