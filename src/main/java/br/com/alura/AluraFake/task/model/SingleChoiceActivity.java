package br.com.alura.AluraFake.task.model;

import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("SINGLE_CHOICE")
public class SingleChoiceActivity extends Activity {

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Option> options = new LinkedHashSet<>();

    @Override
    public String getType() {
        return "SINGLE_CHOICE";
    }

    @Deprecated
    public SingleChoiceActivity() {
        super();
    }

    public SingleChoiceActivity(String statement, Integer order, Course course) {
        super(statement, order, course);
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void addOption(String text, boolean isCorrect) {
        this.options.add(new Option(text, isCorrect, this));
    }
}
