package co.uk.pbnj.dashin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class DisplayItem {
    private String name;
    private String type;
    private String location;
}
