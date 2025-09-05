package br.com.alura.AluraFake.task.repository;

import br.com.alura.AluraFake.course.model.Course;
import br.com.alura.AluraFake.mocks.MockTaskFactory;
import br.com.alura.AluraFake.task.model.Activity;
import br.com.alura.AluraFake.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Course course;

    @BeforeEach
    void setUp() {
        course = MockTaskFactory.createValidCourse();
        testEntityManager.persist(course.getInstructor());
        testEntityManager.persist(course);
    }



    @Test
    void existsByCourseIdAndStatement_WhenActivityExists_ShouldReturnTrue() {

        //Arrange
        Activity activity = MockTaskFactory.createOpenTextActivity(course, 1);
        testEntityManager.persistAndFlush(activity);

        //Act
        boolean exists = activityRepository.existsByCourseIdAndStatement(course.getId(), activity.getStatement());

        //Assert
        assertThat(exists).isTrue();

    }

    @Test
    void existsByCourseIdAndStatement_WhenActivityDoesNotExist_ShouldReturnFalse() {

        //Act
        boolean exists = activityRepository.existsByCourseIdAndStatement(course.getId(), "Enunciado Inexistente");

        //Assert
        assertThat(exists).isFalse();

    }

    @Test
    void countByCourseId_WhenActivitiesExist_ShouldReturnCorrectCount() {

        //Arrange
        testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 1));
        testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 2));

        //Act
        Long count = activityRepository.countByCourseId(course.getId());

        //Assert
        assertThat(count).isEqualTo(2L);

    }

    @Test
    void findAllByCourseIdOrderByOrderAsc_ShouldReturnSortedActivities() {

        // Arrange
        testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 3));
        testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 1));
        testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 2));

        //Act
        List<Activity> activities = activityRepository.findAllByCourseIdOrderByOrderAsc(course.getId());

        //Assert
        assertThat(activities).hasSize(3);
        assertThat(activities).isSortedAccordingTo((a1, a2) -> a1.getOrder().compareTo(a2.getOrder()));
        assertThat(activities.get(0).getOrder()).isEqualTo(1);

    }

    @Test
    void shiftOrdersForward_ShouldIncrementCorrectOrders()  {

        //Arrange
        Activity act1 = testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 1));
        Activity act2 = testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 2));
        Activity act3 = testEntityManager.persistAndFlush(MockTaskFactory.createOpenTextActivity(course, 3));

        //Act
        activityRepository.shiftOrdersForward(course.getId(), 2);

        testEntityManager.flush();
        testEntityManager.clear();

        //Assert
        Activity updatedAct1 = activityRepository.findById(act1.getId()).get();
        Activity updatedAct2 = activityRepository.findById(act2.getId()).get();
        Activity updatedAct3 = activityRepository.findById(act3.getId()).get();

        assertThat(updatedAct1.getOrder()).isEqualTo(1);
        assertThat(updatedAct2.getOrder()).isEqualTo(3);
        assertThat(updatedAct3.getOrder()).isEqualTo(4);

    }
}