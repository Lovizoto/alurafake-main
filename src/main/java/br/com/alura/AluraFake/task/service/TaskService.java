package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.task.dto.SingleChoiceDTO;
import br.com.alura.AluraFake.task.mapper.ActivityMapper;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public TaskService(CourseRepository courseRepository, ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Transactional
    public void createSingleChoiceActivity(SingleChoiceDTO singleChoiceDTO) {

        Course course =




    }


    private Course findValidCourseById(Long courseId) {

    }


}
