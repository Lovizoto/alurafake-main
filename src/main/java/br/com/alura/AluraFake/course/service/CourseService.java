package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.task.model.MultipleChoiceActivity;
import br.com.alura.AluraFake.task.model.OpenTextActivity;
import br.com.alura.AluraFake.task.model.SingleChoiceActivity;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;

    public CourseService(CourseRepository courseRepository, ActivityRepository activityRepository) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
    }


    @Transactional
    public void publishCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));

        if (course.getStatus() != Status.BUILDING) {
            throw new BusinessRuleException("Course status is not BUILDING");
        }

        List<Activity> activities = activityRepository.findAllByCourseIdOrderByOrderAsc(course.getId());

        validateActivityList(activities);

        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        courseRepository.save(course);

    }

    private void validateActivityList(List<Activity> activities) {
        if (activities == null || activities.isEmpty()) {
            throw new  BusinessRuleException("Course must have at least one activity to be published.");
        }
        validateTypeOfActivity(activities);
        validateSequenceOfOrderInActivity(activities);
    }


    private void validateTypeOfActivity(List<Activity> activities) {

        //atividades de qualquer tipo: open_text, single_choice, multiple_choice
        Set<Class<? extends Activity>> activityTypes = activities.stream()
                .map(Activity::getClass)
                .collect(Collectors.toSet());

        boolean containsOpenText = activityTypes.contains(OpenTextActivity.class);
        boolean containsSingleChoice = activityTypes.contains(SingleChoiceActivity.class);
        boolean containsMultipleChoice = activityTypes.contains(MultipleChoiceActivity.class);

        if (!containsOpenText || !containsSingleChoice || !containsMultipleChoice) {
            throw new BusinessRuleException("Course must contains at least one of each type of activity to be published on platform");
        }

    }

    private void validateSequenceOfOrderInActivity(List<Activity> activities) {
        for (int i = 0; i < activities.size(); i++) {
            int order = i + 1;
            if (activities.get(i).getOrder() != order) {
                throw new BusinessRuleException("The sequence order of activity is not continuous." +
                        "The order expected is " + order + ", but was found " + activities.get(i).getOrder());
            }
        }
    }






}
