package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.cgmconsulting.myblog.payload.request.SignInRequest;
import it.cgmconsulting.myblog.payload.request.SignUpRequest;
import it.cgmconsulting.myblog.payload.response.JwtAuthenticationResponse;
import it.cgmconsulting.myblog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthService authService;

    @Operation(
            summary = "ADD A NEW USER",
            description = "Method to add a new user to the database with a sign in procedure and send an email to confirm the registration",
            tags = {"Tags"})
    @PostMapping("/v0/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @Operation(
            summary = "GET VISIBLE TAGS",
            description = "Method to get a list of all visible tags from the database",
            tags = {"Tags"})
    //TODO: non è un patch!
    @GetMapping("/v0/auth/confirm/{confirmCode}")
    public ResponseEntity<String> verifyEmail(@PathVariable String confirmCode){
        return ResponseEntity.ok(authService.verifyEmail(confirmCode));
    }

    @PostMapping("/v0/auth/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SignInRequest request){
        return ResponseEntity.ok(authService.signin(request));
    }

    @PatchMapping("/v1/auth/modify_user_authority") // modify user authority
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> modifyUserAuthority(@RequestParam int id, @RequestParam String auth){
        return ResponseEntity.ok(authService.modifyUserAuthority(id, auth));
    }



}
