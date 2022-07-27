package io.dcisar.backend.technology;

import io.dcisar.backend.project.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Language")
public class Language extends Technology{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "languagesInProject")
    private List<Project> projects = new ArrayList<>();

    public Language(String name, String description, String version) {
        super(name, description, version);
    }

}
