package br.com.alura.AluraFake.instructor.dto;

import java.util.List;

public record InstructorReportDTO(
        long totalCoursesPublished,
        List<InstructorCourseInfoDTO> courses
) {}
