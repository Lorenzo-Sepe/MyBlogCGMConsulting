package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest (
    @NotBlank(message = "The Username must not be null or blank")
    @Size(max=30,min=3,message = "The Username must be between 3 and 30 characters")
     String username,

    @Email
    @NotBlank(message = "The email must not be null or blank")
    String email,

    @NotBlank(message = "The password must not be null or blank")
    @Size(max=16,min=8,message = "The password must be between 6 and 30 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[@#$%£^&!?+-]).*$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    String password
){

}
