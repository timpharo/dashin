package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.Todo;

public record TaskResponse(int order, String content, String description){

    public Todo toTask(){
        return new Todo(
                order,
                content,
                description
        );
    }
}
