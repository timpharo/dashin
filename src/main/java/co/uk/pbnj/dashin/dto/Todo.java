package co.uk.pbnj.dashin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class Todo {
    private int order;
    private String content;
    private String description;
}
