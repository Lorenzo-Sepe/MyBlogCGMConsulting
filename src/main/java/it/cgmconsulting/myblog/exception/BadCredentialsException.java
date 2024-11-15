package it.cgmconsulting.myblog.exception;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException {

    private String messageError;

    public BadCredentialsException(String messageError) {
        this.messageError = messageError;
    }
}
