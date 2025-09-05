package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.mocks.MockTaskFactory;
import br.com.alura.AluraFake.task.dto.OpenTextDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceDTO;
import br.com.alura.AluraFake.task.mapper.ActivityMapper;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test for TaskService")
class TaskServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createOpenTextActivity_withValidData_shouldSucceed() {

        //Arrange
        OpenTextDTO validDTO = MockTaskFactory.createValidOpenTextDTO();
        Course validCourse = MockTaskFactory.createValidCourse();

        //Arrange
        when(courseRepository.findById(validDTO.courseId())).thenReturn(Optional.of(validCourse));
        when(activityRepository.existsByCourseIdAndStatement(any(), any())).thenReturn(false);
        when(activityRepository.countByCourseId(any())).thenReturn(0L);
        when(activityMapper.toEntity(any(OpenTextDTO.class))).thenReturn(MockTaskFactory.createOpenTextActivity(validCourse, 1));

        //Act
        taskService.createOpenTextActivity(validDTO);

        //Assert
        verify(activityRepository, times(1)).save(any(Activity.class));
        verify(activityRepository, never()).shiftOrdersForward(any(), any());

    }


    @Test
    void createSingleChoiceActivity_whenCourseNotFound_shouldThrowException() {

        //Arrange
        SingleChoiceDTO singleChoiceDTO = MockTaskFactory.createValidSingleChoiceDTO();

        when(courseRepository.findById(singleChoiceDTO.courseId())).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(ResourceNotFoundException.class, () ->{
            taskService.createSingleChoiceActivity(singleChoiceDTO);
        });

        verify(activityRepository, never()).save(any());
        verify(activityMapper, never()).toEntity(any(SingleChoiceDTO.class));

    }

    @Test
    void createActivity_WhenStatementIsDuplicate_ShouldThrowException() {

        //Arrange
        SingleChoiceDTO singleChoiceDTO = MockTaskFactory.createValidSingleChoiceDTO();
        Course validCourse = MockTaskFactory.createValidCourse();
        when(courseRepository.findById(singleChoiceDTO.courseId())).thenReturn(Optional.of(validCourse));

        when(activityRepository.existsByCourseIdAndStatement(validCourse.getId(), singleChoiceDTO.statement())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> {
            taskService.createSingleChoiceActivity(singleChoiceDTO);
        });

        verify(activityRepository, never()).save(any());

    }

}