package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record OptionDTO(@NotBlank @Length(min = 4, max = 80) String text, boolean isCorrect) {

}
