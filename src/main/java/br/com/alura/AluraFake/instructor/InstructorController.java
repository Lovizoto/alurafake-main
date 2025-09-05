package br.com.alura.AluraFake.instructor;

import br.com.alura.AluraFake.instructor.dto.InstructorReportDTO;
import br.com.alura.AluraFake.instructor.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<InstructorReportDTO> getCourseReport(@PathVariable("id") Long id) {
        InstructorReportDTO report = instructorService.createCoursesReport(id);
        return ResponseEntity.ok(report);
    }

}
