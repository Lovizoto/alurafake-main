package br.com.alura.AluraFake.course.repository;

import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.task.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    long countByInstructorIdAndStatus(Long instructorId, Status status);

    List<Course> findAllByInstructorId(Long instructorId);
}
