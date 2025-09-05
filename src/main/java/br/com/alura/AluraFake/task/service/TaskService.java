package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.BusinessRuleException;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.task.dto.*;
import br.com.alura.AluraFake.task.mapper.ActivityMapper;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.task.model.MultipleChoiceActivity;
import br.com.alura.AluraFake.task.model.OpenTextActivity;
import br.com.alura.AluraFake.task.model.SingleChoiceActivity;
import br.com.alura.AluraFake.task.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ActivityResponseDTO createOpenTextActivity(OpenTextDTO openTextDTO) {
        Course course = findValidCourseById(openTextDTO.courseId());
        verifyCommomRules(course, openTextDTO.statement(), openTextDTO.order());
        handleShiftingOrder(course.getId(), openTextDTO.order());

        OpenTextActivity openTextActivity = activityMapper.toEntity(openTextDTO);
        Activity activity = activityRepository.save(openTextActivity);

        return activityMapper.toActivityResponseDTO(activity);
    }

    @Transactional
    public ActivityResponseDTO createSingleChoiceActivity(SingleChoiceDTO singleChoiceDTO) {

        Course course = findValidCourseById(singleChoiceDTO.courseId());

        verifyCommomRules(course, singleChoiceDTO.statement(), singleChoiceDTO.order());
        verifySingleChoiceOptions(singleChoiceDTO.statement(), singleChoiceDTO.options());
        handleShiftingOrder(course.getId(), singleChoiceDTO.order());

        SingleChoiceActivity singleChoiceActivity = activityMapper.toEntity(singleChoiceDTO);
        singleChoiceActivity.getOptions().forEach(option -> option.setActivity(singleChoiceActivity));

        Activity activity = activityRepository.save(singleChoiceActivity);

        return activityMapper.toActivityResponseDTO((SingleChoiceActivity) activity);
    }

    @Transactional
    public ActivityResponseDTO createMultipleChoiceActivity(MultipleChoiceDTO multipleChoiceDTO) {
        Course course = findValidCourseById(multipleChoiceDTO.courseId());
        verifyCommomRules(course, multipleChoiceDTO.statement(), multipleChoiceDTO.order());
        verifyMultipleChoiceOptions(multipleChoiceDTO.statement(), multipleChoiceDTO.options());
        handleShiftingOrder(course.getId(), multipleChoiceDTO.order());

        MultipleChoiceActivity multipleChoiceActivity = activityMapper.toEntity(multipleChoiceDTO);
        multipleChoiceActivity.getOptions().forEach(option -> option.setActivity(multipleChoiceActivity));

        Activity activity = activityRepository.save(multipleChoiceActivity);

        return activityMapper.toActivityResponseDTO((MultipleChoiceActivity) activity);
    }


    /*
        Inicialmente eu pensei em criar uma classe TaskValidator com os métodos abaixo.
        Porém, a única classe que utiliza estes métodos é o próprio TaskService,
        ou seja, a classe é  o orquestrador/especialista para uso destes métodos, delegar
        uma outra classe seria utilizar um ajudante.
        Pense que o Ranger no D&D é o especialista em armadilhas, ele não delega
        alguém do grupo para armá-las, podemos compreender estes métodos como as especialidades
        da sua classe.
     */


    private Course findValidCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id: " + courseId + " not found"));
        if (course.getStatus() != Status.BUILDING) {
            throw new BusinessRuleException("Course must be in BUILDING status to receive activities");
        }
        return course;
    }

    private void verifyCommomRules(Course course, String statement, int order) {
        if (activityRepository.existsByCourseIdAndStatement(course.getId(), statement)) {
            throw new BusinessRuleException("Statement already exists for this course.");
        }

        int activityCount = activityRepository.countByCourseId(course.getId()).intValue();
        if (order > activityCount + 1) {
            throw new BusinessRuleException("Invalid order. Next valid order in sequence is " + (activityCount +1));
        }

    }

    private void handleShiftingOrder(Long courseId, int order) {
        int activityCount = activityRepository.countByCourseId(courseId).intValue();
        if (order <= activityCount) {
            activityRepository.shiftOrdersForward(courseId, order);
        }
    }

    private void verifyOptions(String statement, List<OptionDTO> options) {

        boolean matchStatement = options.stream()
                .anyMatch(o -> o.text().equalsIgnoreCase(statement));
        if (matchStatement) {
            throw new BusinessRuleException("Options cannot have the same text as the statement.");
        }

        Set<String> uniqueTextInOptions = options.stream()
                .map(OptionDTO::text)
                .collect(Collectors.toSet());

        if (uniqueTextInOptions.size() < options.size()) {
            throw new BusinessRuleException("Duplicate options are not allowed.");
        }

    }

    private void verifySingleChoiceOptions(String statement, List<OptionDTO> options) {
        verifyOptions(statement, options);
        long correctCount = options.stream().filter(OptionDTO::isCorrect).count();
        if (correctCount != 1) {
            throw new BusinessRuleException("Single choice activity must have only one correct text.");
        }
    }

    private void verifyMultipleChoiceOptions(String statement, List<OptionDTO> options) {
        verifyOptions(statement, options);
        long correctCount = options.stream().filter(OptionDTO::isCorrect).count();
        long incorrectCount = options.size() - correctCount; //Diferença = TotalLista - Corretas = Incorretas

        if (correctCount < 2 || incorrectCount < 1) {
            throw new BusinessRuleException("Multiple choice at least must contains 1 or 2 correct options.");
        }

    }



}
