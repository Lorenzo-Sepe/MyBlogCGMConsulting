package it.cgmconsulting.myblog.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class DisabledException extends RuntimeException{

    private final String messageError;
    public DisabledException(String messageError){
        this.messageError = messageError;
    }
}
