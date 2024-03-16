package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.PrevCloseResult;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record PrevCloseResultResponse(Double o, Double c){

    public PrevCloseResult toPrevCloseResult(){
        return new PrevCloseResult(o, c);
    }
}
