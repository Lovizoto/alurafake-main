package br.com.alura.AluraFake.task.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Option {

    @Id
    private Long id;

    private String option;

    private Boolean isCorrect;



}
