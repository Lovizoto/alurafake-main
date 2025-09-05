package br.com.alura.AluraFake.task;


import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.course.repository.CourseRepository;
import br.com.alura.AluraFake.mocks.MockTaskFactory;
import br.com.alura.AluraFake.task.dto.OpenTextDTO;

import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.UserRepository;
import br.com.alura.AluraFake.user.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    private Course courseInBuilding;

    @BeforeEach
    void setUp() {

        User instructor = MockTaskFactory.createValidInstructor();
        userRepository.save(instructor);

        courseInBuilding = new Course("Curso de Integração", "Testando o fluxo completo", instructor);
        courseInBuilding.setStatus(Status.BUILDING);
        courseRepository.save(courseInBuilding);
    }

    @Test
    @DisplayName("Deve criar uma atividade de texto aberto com sucesso e retornar status 201")
    void createOpenTextActivity_withValidData_returnsCreated() throws Exception {

        //Arrange
        var requestDTO = MockTaskFactory.createValidOpenTextDTO(courseInBuilding.getId());
        String requestJson = objectMapper.writeValueAsString(requestDTO);


        //Act and Assert
        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated()) // Espera o status 201 Created
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists()) // O corpo da resposta deve ter o ID gerado
                .andExpect(jsonPath("$.statement").value(requestDTO.statement()))
                .andExpect(jsonPath("$.type").value("OPEN_TEXT"))
                .andExpect(jsonPath("$.courseId").value(courseInBuilding.getId()));
    }

    @Test
    @DisplayName("Deve falhar ao criar atividade com enunciado duplicado e retornar 400")
    void createOpenTextActivity_withDuplicateStatement_returnsBadRequest() throws Exception {

        //Arrange
        var firstRequestDTO = MockTaskFactory.createValidOpenTextDTO(courseInBuilding.getId());
        mockMvc.perform(post("/task/new/opentext").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(firstRequestDTO)));


        var secondRequestDTO = MockTaskFactory.createValidOpenTextDTO(courseInBuilding.getId());
        String secondRequestJson = objectMapper.writeValueAsString(secondRequestDTO);

        //Act and assert
        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Statement already exists for this course."));
    }
}