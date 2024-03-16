package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.dto.TodoBuilder;

public record TaskResponse(int order, String content, String description){

    public Todo toTask(){
        return TodoBuilder.builder()
                .order(order)
                .content(content)
                .description(description)
                .build();
    }
}
