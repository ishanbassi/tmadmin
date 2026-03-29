package com.bassi.tmapp.web.rest.errors;

public class OtpNotReceivedException extends RuntimeException {

    public OtpNotReceivedException(String message) {
        super(message);
    }

    public OtpNotReceivedException(String message, Throwable cause) {
        super(message, cause);
    }
}
