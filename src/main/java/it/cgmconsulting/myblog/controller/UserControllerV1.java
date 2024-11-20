package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.response.UserResponse;
import it.cgmconsulting.myblog.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserControllerV1 {

    private final UserService userService;

    @GetMapping("/v1/user")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.getMe(userDetails));
    }

    // cambiare la password
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

    // cambio username / email
    @PatchMapping("/v1/users")
    public ResponseEntity<UserResponse> changeUsernameAndEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotBlank(message = "Username non può essere nullo") @Size(max = 20, min=3, message = "Username di lunghezza non idonea. Scegliere un Username compreso tra 3 e 20 charatteri") String username,
            @RequestParam @Email(message = "Email non valida") String email
            ){
        return ResponseEntity.ok(userService.changeUsernameAndEmail(userDetails,username, email));
    }

    // aggiornamento avatar

}
