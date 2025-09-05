package br.com.alura.AluraFake.task.repository;

import br.com.alura.AluraFake.task.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    boolean existsByCourseIdAndStatement(Long courseId, String statement);

    Long countByCourseId(Long id);

    @Modifying
    @Query("UPDATE Activity a SET a.order = a.order + 1 WHERE a.course.id = :courseId AND a.order >= :order")
    void shiftOrdersForward(Long courseId, Integer order);

    List<Activity> findAllByCourseIdOrderByOrderAsc(Long id);
}
