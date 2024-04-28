package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.Project;

public record ProjectResponse(String id, String name, int comment_count){
    public Project toProject(){
        return new Project(
                id,
                name,
                comment_count
            );
    }
}
