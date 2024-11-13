package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {
    private final AuthService authService;
    
    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid SignUpRequest request) {
        return new ResponseEntity<>(authService.signup(request), HttpStatus.OK);
    }
    
}
