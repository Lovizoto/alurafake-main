package br.com.alura.AluraFake.task.model;

import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenTextActivity extends Activity {

    @Override
    public String getType() {
        return "OPEN_TEXT";
    }

    @Deprecated
    public OpenTextActivity() {
    }

    public OpenTextActivity(Long id, String statement, Integer order, Course course) {
        super(id, statement, order, course);
    }

}
