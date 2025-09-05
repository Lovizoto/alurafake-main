package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.mocks.MockTaskFactory;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.task.model.MultipleChoiceActivity;
import br.com.alura.AluraFake.task.model.OpenTextActivity;
import br.com.alura.AluraFake.task.model.SingleChoiceActivity;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ActivityRepository activityRepository;
    @InjectMocks
    private CourseService courseService;

    @Test
    void publishCourse_WithValidBuildingCourse_ShouldPublishSuccessfully() {

        // Arrange
        Course course = MockTaskFactory.createValidCourse();
        course.setId(1L);
        List<Activity> activities = MockTaskFactory.createValidActivitiesList(course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(activityRepository.findAllByCourseIdOrderByOrderAsc(1L)).thenReturn(activities);

        // Act
        courseService.publishCourse(1L);

        // Assert
        assertEquals(Status.PUBLISHED, course.getStatus());
        assertNotNull(course.getPublishedAt());
        verify(courseRepository).save(course);
    }

    @Test
    void publishCourse_WithNonBuildingCourse_ShouldThrowBusinessRuleException() {
        // Arrange
        Course publishedCourse = MockTaskFactory.createValidCourse();
        publishedCourse.setStatus(Status.PUBLISHED);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(publishedCourse));

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> courseService.publishCourse(1L));

        assertEquals("Course status is not BUILDING", exception.getMessage());
        verify(activityRepository, never()).findAllByCourseIdOrderByOrderAsc(any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void publishCourse_WithNonExistentCourse_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.publishCourse(99L));

        assertEquals("Course not found with id 99", exception.getMessage());
        verify(activityRepository, never()).findAllByCourseIdOrderByOrderAsc(any());
        verify(courseRepository, never()).save(any());
    }
}