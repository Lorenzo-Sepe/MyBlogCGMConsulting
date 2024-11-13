package it.cgmconsulting.myblog.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagment {

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<String> conflictExceptionManagement(ConflictException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<List<String>> constraintViolationExceptionManagement(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return new ResponseEntity<List<String>>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> ResurceNotFoundExceptionManagement(ConflictException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}