package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "CHANGE PASSWORD OF USER",
            description = "Method to change the password of the user in the database",
            tags = {"User"})
    @PatchMapping("/v1/users/change_pwd")
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

    @Operation(
            summary = "GET USER INFO OF CURRENT USER",
            description = "Method to get the information of the current logged user",
            tags = {"User"})
  @GetMapping("/v1/users")
  public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.getMe(userDetails));
  }

  @Operation(
            summary = "CHANGE USERNAME AND OR EMAIL OF USER",
            description = "Method to change the username and or email of the user in the database",
            tags = {"User"})
  @PatchMapping("v1/users")
  public ResponseEntity<UserResponse> changeUsernameAndEmail(
          @AuthenticationPrincipal UserDetails userDetails,
          @RequestParam @NotBlank @Size(max = 20, min = 3) String username,
          @RequestParam @Email String email){
        return ResponseEntity.ok(userService.changeUsernameAndEmail(userDetails, username, email));
  }




}
