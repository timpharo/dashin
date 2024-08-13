package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.PrevCloseStock;

import java.util.List;

public record PrevCloseStockResponse(List<PrevCloseResultResponse> results){

    public PrevCloseStock toPrevCloseStock() {
        return new PrevCloseStock(
                results.stream()
                        .map(PrevCloseResultResponse::toDailyOpenCloseResult).toList()
        );
    }
}
