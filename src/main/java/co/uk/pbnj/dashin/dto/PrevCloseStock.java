package co.uk.pbnj.dashin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PrevCloseStock {
    private List<PrevCloseResult> results;
}
