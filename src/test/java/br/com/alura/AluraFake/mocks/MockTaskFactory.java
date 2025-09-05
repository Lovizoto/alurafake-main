package br.com.alura.AluraFake.mocks;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.task.dto.MultipleChoiceDTO;
import br.com.alura.AluraFake.task.dto.OpenTextDTO;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceDTO;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.task.model.MultipleChoiceActivity;
import br.com.alura.AluraFake.task.model.OpenTextActivity;
import br.com.alura.AluraFake.task.model.SingleChoiceActivity;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.model.User;

import java.util.List;

public class MockTaskFactory {

    public static final Long validCourseId = 1L;
    public static final Long publishedCourseId = 2L;
    public static final Long nonExistentCourseId = 99L;
    public static final Long validInstructorId = 10L;

    public static User createValidInstructor() {
        User instructor = new User("Instrutor Teste", "instrutor@test.com", Role.INSTRUCTOR, "123456");
        return instructor;
    }

    public static Course createValidCourse() {
        Course course = new Course("Curso Teste", "Testar JNUnit5", createValidInstructor());
        course.setStatus(Status.BUILDING);
        return course;
    }

    public static OpenTextActivity createOpenTextActivity(Course course, int order) {
        return new OpenTextActivity("Statement da atividade" + order,
                order,
                course);
    }

    public static OpenTextDTO createValidOpenTextDTO() {
        return new OpenTextDTO(validCourseId,
                "Statement aberto válido",
                1);
    }

    public static OpenTextDTO createValidOpenTextDTO(Long courseId) {
        return new OpenTextDTO(courseId,
                "Statement aberto válido",
                1);
    }

    public static SingleChoiceDTO createValidSingleChoiceDTO() {
        var options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Python", false)
        );
        return new SingleChoiceDTO(
                validCourseId,
                "Linguagem principal do ecossistema Spring",
                1,
                options
        );
    }

    public static MultipleChoiceDTO createValidMultipleChoiceDTO() {
        var options = List.of(
                new OptionDTO("Interface", true),
                new OptionDTO("Classe Abstrata", true),
                new OptionDTO("Record", false)
        );
        return new MultipleChoiceDTO(
                validCourseId,
                "Mecanismos de abstração em Java",
                1,
                options
        );
    }

    /*
        Teste de validações
     */
    public static SingleChoiceDTO createSingleChoiceDTOWithInvalidStatement() {
        return new SingleChoiceDTO(
                validCourseId,
                "oi",
                1,
                List.of(new OptionDTO("Opção 1", true),
                        new OptionDTO("Opção 2", false))
        );
    }


    public static SingleChoiceDTO createSingleChoiceDTOWithDuplicateOptions() {
        var options = List.of(
                new OptionDTO("Duplicada", true),
                new OptionDTO("Outra opção", false),
                new OptionDTO("Duplicada", false)
        );
        return new SingleChoiceDTO(
                validCourseId,
                "Teste de Duplicatas",
                1,
                options
        );
    }

    public static List<Activity> createValidActivitiesList(Course course) {
        return List.of(
                MockTaskFactory.createOpenTextActivity(course, 1),
                createSingleChoiceActivity(course, 2),
                createMultipleChoiceActivity(course, 3)
        );
    }

    private static SingleChoiceActivity createSingleChoiceActivity(Course course, int order) {
        SingleChoiceActivity activity = new SingleChoiceActivity(
                "Enunciado Single Choice " + order, order, course
        );
        activity.setId((long) order * 10 + 2);
        return activity;
    }

    private static MultipleChoiceActivity createMultipleChoiceActivity(Course course, int order) {
        MultipleChoiceActivity activity = new MultipleChoiceActivity(
                "Enunciado Multiple Choice " + order, order, course
        );
        activity.setId((long) order * 10 + 3);
        return activity;
    }


}
