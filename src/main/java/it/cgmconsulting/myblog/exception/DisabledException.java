package it.cgmconsulting.myblog.exception;

import lombok.Getter;

@Getter
public class DisabledException extends RuntimeException {

    private String messageError;

    public DisabledException(String messageError) {
        this.messageError = messageError;
    }
}
