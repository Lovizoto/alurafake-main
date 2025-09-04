package br.com.alura.AluraFake.task.model;


import br.com.alura.AluraFake.course.model.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
@DiscriminatorColumn(name = "`TYPE`")
public abstract class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Length(min = 4, max = 255)
    @NotBlank
    private String statement; //enunciado da questão

    @Column(name = "`order`", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //optional false vai evitar de salvar nulo no course_id
    @JoinColumn(name = "course_id")
    private Course course;

    public abstract String getType();

    @Deprecated
    public Activity() {
    }

    public Activity(String statement, Integer order, Course course) {
        this.statement = statement;
        this.order = order;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
