package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record PrevCloseStock(List<PrevCloseResult> results){

}
