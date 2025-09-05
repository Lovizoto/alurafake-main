package br.com.alura.AluraFake.instructor.dto;

import br.com.alura.AluraFake.course.Status;

import java.time.LocalDateTime;

public record InstructorCourseInfoDTO(
        Long id,
        String title,
        Status status,
        LocalDateTime publishedAt,
        long activityCount
) {}
