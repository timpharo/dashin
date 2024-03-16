package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record DisplayItem(String name, String type, String location) {
}
