package br.com.alura.AluraFake.task.model;

import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceActivity extends Activity {

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> options = new LinkedHashSet<>();

    @Override
    public String getType() {
        return "MULTIPLE_CHOICE";
    }

    @Deprecated
    public MultipleChoiceActivity() {
        super();
    }

    public MultipleChoiceActivity(String statement, Integer order, Course course) {
        super(statement, order, course);
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void addOption(String text, boolean isCorrect) {
        this.options.add(new Option(text, isCorrect, this));
    }



}
