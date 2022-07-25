package io.dcisar.backend.technology;

import io.dcisar.backend.project.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Technology")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "technologiesInProject")
    private List<Project> projects = new ArrayList<>();

    public Technology(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
