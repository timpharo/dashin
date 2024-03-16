package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Project(String id, String name, int comment_count) {
}
