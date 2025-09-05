package br.com.alura.AluraFake.instructor.service;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.instructor.dto.InstructorCourseInfoDTO;
import br.com.alura.AluraFake.instructor.dto.InstructorReportDTO;
import br.com.alura.AluraFake.instructor.mapper.InstructorMapper;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import br.com.alura.AluraFake.user.UserRepository;
import br.com.alura.AluraFake.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ActivityRepository activityRepository;
    private final InstructorMapper instructorMapper;

    public InstructorService(UserRepository userRepository, CourseRepository courseRepository, ActivityRepository activityRepository, InstructorMapper instructorMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.instructorMapper = instructorMapper;
    }


    @Transactional(readOnly = true)
    public InstructorReportDTO createCoursesReport(Long instructorId) {

        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + instructorId));

        if (!instructor.isInstructor()) {
            throw new BusinessRuleException("User is not instructor");
        }

        long totalCoursesPublished = courseRepository.countByInstructorIdAndStatus(instructorId, Status.PUBLISHED);
        List<InstructorCourseInfoDTO> courseInfo = courseRepository.findAllByInstructorId(instructorId)
                .stream()
                .map(course -> {
                    long activityCount = activityRepository.countByCourseId(course.getId());
                    return instructorMapper.toInfoDTO(course, activityCount);
                }).collect(Collectors.toList());
        return new InstructorReportDTO(totalCoursesPublished, courseInfo);

    }



}
