package br.com.alura.AluraFake.task.mapper;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.task.dto.*;
import br.com.alura.AluraFake.task.model.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ActivityMapper {

    @Autowired
    protected CourseRepository courseRepository;


    @Mapping(source="courseId", target="course")
    public abstract OpenTextActivity toEntity(OpenTextDTO dto);

    @Mapping(source="courseId", target="course")
    public abstract SingleChoiceActivity toEntity(SingleChoiceDTO dto);

    @Mapping(source="courseId", target="course")
    public abstract MultipleChoiceActivity toEntity(MultipleChoiceDTO dto);


    protected Course courseIdToCourse(Long id) {
        if (id == null) {
            return null;
        }
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Mapping(source = "text", target = "text")
    @Mapping(source = "isCorrect", target = "isCorrect")
    public abstract OptionDTO toOptionDTO(Option entity);

    @Mapping(source = "text", target = "text")
    @Mapping(source = "isCorrect", target = "isCorrect")
    public abstract Option toOptionEntity(OptionDTO dto);

    @Mapping(target = "type", expression = "java(activity.getType())")
    @Mapping(source = "course.id", target = "courseId")
    public abstract ActivityResponseDTO toActivityResponseDTO(Activity activity);

    @Mapping(target = "type", expression = "java(activity.getType())")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "options", target = "options")
    public abstract ActivityResponseDTO toActivityResponseDTO(SingleChoiceActivity activity);

    @Mapping(target = "type", expression = "java(activity.getType())")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "options", target = "options")
    public abstract ActivityResponseDTO toActivityResponseDTO(MultipleChoiceActivity activity);


}
