package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserControllerV1 {

    private final UserService userService;

    @PatchMapping("/v1/user/change_pwd")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String oldPwd,
            @RequestParam @NotBlank(message = "Password cannot be null or blank")
            @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
            @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+=!]).*$",
                    message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character")
            String newPwd,
            String newPwd2){
        return ResponseEntity.ok(userService.changePwd(userDetails, oldPwd, newPwd, newPwd2));

    }

    // cambiare la password

    // cambio username / email

    // aggiornamento avatar

}
