package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.dto.ActivityResponseDTO;
import br.com.alura.AluraFake.task.dto.MultipleChoiceDTO;
import br.com.alura.AluraFake.task.dto.OpenTextDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/new/opentext")
    public ResponseEntity<ActivityResponseDTO> newOpenTextExercise(@RequestBody @Valid OpenTextDTO openTextDTO) {

        ActivityResponseDTO activityResponseDTO = taskService.createOpenTextActivity(openTextDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(activityResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(activityResponseDTO);
    }

    @PostMapping("/new/singlechoice")
    public ResponseEntity<ActivityResponseDTO> newSingleChoice(@RequestBody @Valid SingleChoiceDTO singleChoiceDTO) {

        ActivityResponseDTO activityResponseDTO = taskService.createSingleChoiceActivity(singleChoiceDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(activityResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(activityResponseDTO);
    }

    @PostMapping("/new/multiplechoice")
    public ResponseEntity<ActivityResponseDTO> newMultipleChoice(@RequestBody @Valid MultipleChoiceDTO multipleChoiceDTO) {

        ActivityResponseDTO activityResponseDTO = taskService.createMultipleChoiceActivity(multipleChoiceDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(activityResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(activityResponseDTO);
    }

}