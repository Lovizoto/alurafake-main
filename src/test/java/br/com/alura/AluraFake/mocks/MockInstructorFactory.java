package br.com.alura.AluraFake.mocks;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.instructor.dto.InstructorCourseInfoDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class MockInstructorFactory {

    public static final Long VALID_INSTRUCTOR_ID = 1L;
    public static final Long NON_INSTRUCTOR_ID = 2L;
    public static final Long NON_EXISTENT_ID = 99L;
    public static final LocalDateTime SAMPLE_PUBLISHED_AT = LocalDateTime.of(2025, 9, 5, 14, 0);

    public static User createValidInstructor() {
        User instructor = new User("Instrutor Teste", "instrutor@test.com", Role.INSTRUCTOR, "123456");
        instructor.setId(VALID_INSTRUCTOR_ID);
        return instructor;
    }

    public static User createNonInstructorUser() {
        User student = new User("Aluno Teste", "aluno@test.com", Role.STUDENT, "123456");
        student.setId(NON_INSTRUCTOR_ID);
        return student;
    }

    public static Course createPublishedCourse(User instructor) {
        Course course = new Course("Curso Publicado", "Descrição do curso publicado", instructor);
        course.setId(1L);
        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(SAMPLE_PUBLISHED_AT);
        return course;
    }

    public static Course createBuildingCourse(User instructor) {
        Course course = new Course("Curso em Construção", "Descrição do curso em construção", instructor);
        course.setId(2L);
        course.setStatus(Status.BUILDING);
        course.setPublishedAt(null);
        return course;
    }

    public static List<Course> createInstructorCourses(User instructor) {
        return List.of(
                createPublishedCourse(instructor),
                createBuildingCourse(instructor)
        );
    }

    public static List<Course> createEmptyCoursesList() {
        return List.of();
    }

    // Métodos para criar InstructorCourseInfoDTO
    public static InstructorCourseInfoDTO createPublishedCourseInfoDTO() {
        return new InstructorCourseInfoDTO(
                1L,
                "Curso Publicado",
                Status.PUBLISHED,
                SAMPLE_PUBLISHED_AT,
                5L
        );
    }

    public static InstructorCourseInfoDTO createBuildingCourseInfoDTO() {
        return new InstructorCourseInfoDTO(
                2L,
                "Curso em Construção",
                Status.BUILDING,
                null,
                3L
        );
    }

    public static InstructorCourseInfoDTO createPublishedCourseInfoDTOWithActivityCount(long activityCount) {
        return new InstructorCourseInfoDTO(
                1L,
                "Curso Publicado",
                Status.PUBLISHED,
                SAMPLE_PUBLISHED_AT,
                activityCount
        );
    }

    public static InstructorCourseInfoDTO createBuildingCourseInfoDTOWithActivityCount(long activityCount) {
        return new InstructorCourseInfoDTO(
                2L,
                "Curso em Construção",
                Status.BUILDING,
                null,
                activityCount
        );
    }

    public static InstructorCourseInfoDTO createCourseInfoDTO(Long id, String title, Status status,
                                                              LocalDateTime publishedAt, long activityCount) {
        return new InstructorCourseInfoDTO(id, title, status, publishedAt, activityCount);
    }

    public static List<InstructorCourseInfoDTO> createSampleCourseInfoList() {
        return List.of(
                createPublishedCourseInfoDTO(),
                createBuildingCourseInfoDTO()
        );
    }

    public static List<InstructorCourseInfoDTO> createEmptyCourseInfoList() {
        return List.of();
    }
}