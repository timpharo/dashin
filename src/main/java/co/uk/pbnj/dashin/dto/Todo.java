package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Todo(int order, String content, String description) {

}
