package br.com.alura.AluraFake.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4, max = 80)
    @Column(name = "option", nullable = false)
    @NotBlank
    private String text;

    private Boolean isCorrect;

    @ManyToOne
    private Activity activity;

    @Deprecated
    public Option() {
    }

    public Option(String text, Boolean isCorrect, Activity activity) {
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

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
