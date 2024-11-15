package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PatchMapping("/confirm/{confirmCode}")
    public ResponseEntity<String> verifyEmail(@PathVariable String confirmCode){
        return ResponseEntity.ok(authService.verifyEmail(confirmCode));
    }

}
