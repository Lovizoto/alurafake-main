package br.com.alura.AluraFake.task.model;


import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

@Entity
public class Activity {

    @Id
    private Long id;

    @Length(min = 4, max = 255)
    private String statement; //enunciado da questão

    private Integer order;


    @JoinColumn(name = "course_id")
    private Course course;



}
