package io.dcisar.backend.framework;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;
import io.dcisar.backend.language.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Framework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String version;
    private String documentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @JsonIgnore
    @ManyToMany(mappedBy = "frameworksInProject")
    private List<Project> projects = new ArrayList<>();
}
