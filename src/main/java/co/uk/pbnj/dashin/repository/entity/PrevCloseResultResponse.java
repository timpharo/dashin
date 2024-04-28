package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.PrevCloseResult;

public record PrevCloseResultResponse(Double o, Double c){

    public PrevCloseResult toPrevCloseResult(){
        return new PrevCloseResult(o, c);
    }
}
