package com.fitness.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 32, message = "Password must not exceed 32 characters") // Increased to 32
    private String password;
    @NotBlank(message = "First name required")
    private String firstName;

    private String lastName;
}
