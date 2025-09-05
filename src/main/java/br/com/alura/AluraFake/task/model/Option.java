package br.com.alura.AluraFake.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Entity
@Table(name = "`option`")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4, max = 80)
    @Column(name = "text", nullable = false)
    @NotBlank
    private String text;

    private boolean isCorrect;

    @ManyToOne
    private Activity activity;

    @Deprecated
    public Option() {
    }

    public Option(String text, boolean isCorrect, Activity activity) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.activity = activity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsCorrect() {
        return this.isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(text, option.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

}
