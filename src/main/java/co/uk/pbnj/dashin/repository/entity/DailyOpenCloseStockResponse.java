package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.DailyOpenCloseResult;
import co.uk.pbnj.dashin.dto.PrevCloseStock;

import java.util.List;

public record DailyOpenCloseStockResponse(Double open, Double close, Double high, Double low) {

    public DailyOpenCloseResult toDailyOpenCloseResult() {
        return new DailyOpenCloseResult(open, close, high, low);
    }
}
