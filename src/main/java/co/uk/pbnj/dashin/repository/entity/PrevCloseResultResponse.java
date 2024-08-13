package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.DailyOpenCloseResult;

public record PrevCloseResultResponse(Double o, Double c, Double h, Double l){

    public DailyOpenCloseResult toDailyOpenCloseResult(){
        return new DailyOpenCloseResult(o, c, h, l);
    }
}
