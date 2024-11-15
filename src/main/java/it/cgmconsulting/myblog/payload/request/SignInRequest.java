package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignInRequest (
        @NotBlank(message = "Username cannot be null or blank")
        String usernameOrEmail,

        @NotBlank(message = "Password cannot be null or blank")
        @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+=!]).*$",
                message = "Password Errata. Controllare il campo inserito.")
        String password


) {
}
