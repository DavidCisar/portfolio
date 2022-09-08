package io.dcisar.backend.topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "topicsInProject")
    private List<Project> projects;

    public void addProjectToTopic(Project project) {
        if (!projects.contains(project)) {
            projects.add(project);
        }
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", projects=" + projects +
                '}';
    }
}