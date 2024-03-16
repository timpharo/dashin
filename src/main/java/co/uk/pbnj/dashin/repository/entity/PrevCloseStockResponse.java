package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.PrevCloseStock;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record PrevCloseStockResponse(List<PrevCloseResultResponse> results){

    public PrevCloseStock toPrevCloseStock() {
        return new PrevCloseStock(
                results.stream()
                        .map(PrevCloseResultResponse::toPrevCloseResult).toList()
        );
    }
}
