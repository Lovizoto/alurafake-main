package br.com.alura.AluraFake.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank(message = "E-mail cannot be blank") @Email(message = "E-mail is not valid") String email,
        @NotBlank(message = "Password cannot be blank") String password) {
}
