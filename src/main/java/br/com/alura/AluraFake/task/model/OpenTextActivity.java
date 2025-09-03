package br.com.alura.AluraFake.task.model;

import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.Entity;

@Entity
public class OpenTextActivity extends Activity {

    @Deprecated
    public OpenTextActivity() {
    }

    public OpenTextActivity(Long id, String statement, Integer order, Course course) {
        super(id, statement, order, course);
    }

}
