package co.uk.pbnj.dashin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DailyOpenCloseResult {
    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
