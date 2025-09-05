package br.com.alura.AluraFake.instructor.service;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.instructor.dto.InstructorReportDTO;
import br.com.alura.AluraFake.instructor.mapper.InstructorMapper;
import br.com.alura.AluraFake.mocks.MockInstructorFactory;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import br.com.alura.AluraFake.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para a classe
@DisplayName("Tests for InstructorService")
public class InstructorServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private InstructorMapper instructorMapper;

    @InjectMocks
    private InstructorService instructorService;

    @Test
    @DisplayName("Deve gerar o relatório de cursos com sucesso para um instrutor válido")
    void createCoursesReport_WithValidInstructor_ShouldReturnReport() {

        //Verificar este test

        // Arrange
        var validInstructor = MockInstructorFactory.createValidInstructor();
        var instructorCourses = MockInstructorFactory.createInstructorCourses(validInstructor);
        var publishedCourse = instructorCourses.get(0);
        var buildingCourse = instructorCourses.get(1);


        when(userRepository.findById(MockInstructorFactory.VALID_INSTRUCTOR_ID)).thenReturn(Optional.of(validInstructor));

        when(courseRepository.findAllByInstructorId(MockInstructorFactory.VALID_INSTRUCTOR_ID)).thenReturn(instructorCourses);


        when(courseRepository.countByInstructorIdAndStatus(MockInstructorFactory.VALID_INSTRUCTOR_ID, Status.PUBLISHED)).thenReturn(1L);
        when(activityRepository.countByCourseId(publishedCourse.getId())).thenReturn(5L);
        when(activityRepository.countByCourseId(buildingCourse.getId())).thenReturn(3L);


        when(instructorMapper.toInfoDTO(publishedCourse, 5L)).thenReturn(MockInstructorFactory.createPublishedCourseInfoDTO());
        when(instructorMapper.toInfoDTO(buildingCourse, 3L)).thenReturn(MockInstructorFactory.createBuildingCourseInfoDTO());

        // Act
        InstructorReportDTO report = instructorService.createCoursesReport(MockInstructorFactory.VALID_INSTRUCTOR_ID);

        //Assert
        assertThat(report).isNotNull();
        assertThat(report.totalCoursesPublished()).isEqualTo(1L);
        assertThat(report.courses()).hasSize(2);
        assertThat(report.courses().get(0).title()).isEqualTo("Curso Publicado");
        assertThat(report.courses().get(0).activityCount()).isEqualTo(5L);
        assertThat(report.courses().get(1).title()).isEqualTo("Curso em Construção");
        assertThat(report.courses().get(1).activityCount()).isEqualTo(3L);

    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException se o usuário não for um instrutor")
    void createCoursesReport_WhenUserIsNotInstructor_ShouldThrowBusinessRuleException() {

        // Arrange
        var nonInstructor = MockInstructorFactory.createNonInstructorUser();
        when(userRepository.findById(MockInstructorFactory.NON_INSTRUCTOR_ID)).thenReturn(Optional.of(nonInstructor));

        // Act and Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            instructorService.createCoursesReport(MockInstructorFactory.NON_INSTRUCTOR_ID);
        });


        assertThat(exception.getMessage()).isEqualTo("User is not instructor");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o usuário não existir")
    void createCoursesReport_WhenUserNotFound_ShouldThrowResourceNotFoundException() {

        // Arrange
        when(userRepository.findById(MockInstructorFactory.NON_EXISTENT_ID)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            instructorService.createCoursesReport(MockInstructorFactory.NON_EXISTENT_ID);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found with id " + MockInstructorFactory.NON_EXISTENT_ID);
    }
}