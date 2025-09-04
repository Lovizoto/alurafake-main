package br.com.alura.AluraFake.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL) //omite campos nulos ou vazios
public record ActivityResponseDTO(
        Long id,
        String statement,
        Integer order,
        String type,
        Long courseId,
        Set<OptionDTO> options) {
}
