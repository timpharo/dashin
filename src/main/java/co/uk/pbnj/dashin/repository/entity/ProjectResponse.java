package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.Project;
import co.uk.pbnj.dashin.dto.ProjectBuilder;

public record ProjectResponse(String id, String name, int comment_count){
    public Project toProject(){
        return ProjectBuilder.builder()
                .id(id)
                .name(name)
                .comment_count(comment_count)
                .build();
    }
}
