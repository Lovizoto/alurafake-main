package br.com.alura.AluraFake.instructor.mapper;


import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.instructor.dto.InstructorCourseInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "activityCount", source = "activityCount")
    InstructorCourseInfoDTO toInfoDTO(Course course, long activityCount);

}
