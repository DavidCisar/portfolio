package io.dcisar.backend.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dcisar.backend.project.Project;
import io.dcisar.backend.framework.Framework;
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
@Table(name = "Language")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String version;
    private String documentation;

    @JsonIgnore
    @OneToMany
    private List<Framework> frameworks;

    @JsonIgnore
    @ManyToMany(mappedBy = "languagesInProject")
    private List<Project> projects = new ArrayList<>();

    public void addFrameworkToLanguage(Framework framework) {
        this.frameworks.add(framework);
    }

    public void removeFrameworkFromLanguage(Framework frameworkToBeDeleted) {
        frameworks.remove(frameworkToBeDeleted);
    }
}
