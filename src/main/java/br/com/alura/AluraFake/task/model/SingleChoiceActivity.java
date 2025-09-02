package br.com.alura.AluraFake.task.model;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SingleChoiceActivity extends Activity {

    List<Option> options = new ArrayList<>();

}
