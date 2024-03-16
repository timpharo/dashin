package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record PrevCloseResult(Double open, Double close){
   
}
