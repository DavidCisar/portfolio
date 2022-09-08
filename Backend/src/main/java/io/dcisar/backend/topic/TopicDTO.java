package io.dcisar.backend.topic;

import io.dcisar.backend.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {

    public Long id;
    public String name;
    public List<Project> projects;

}
