package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record OpenTextDTO(@NotNull Long courseId, @NotBlank @Length(min = 4, max = 255) String statement, @NotNull @Positive Integer order) {
}
