package br.com.alura.AluraFake.task.mapper;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.task.dto.MultipleChoiceDTO;
import br.com.alura.AluraFake.task.dto.OpenTextDTO;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceDTO;
import br.com.alura.AluraFake.task.model.MultipleChoiceActivity;
import br.com.alura.AluraFake.task.model.OpenTextActivity;
import br.com.alura.AluraFake.task.model.Option;
import br.com.alura.AluraFake.task.model.SingleChoiceActivity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    OpenTextActivity toEntity(OpenTextDTO dto, @Context Course course);
    SingleChoiceActivity toEntity(SingleChoiceDTO dto, @Context Course course);
    MultipleChoiceActivity toEntity(MultipleChoiceDTO dto, @Context Course course);


    @Mapping(source = "option", target = "text")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activity", ignore = true)
    Option toOption(OptionDTO dto);




}
