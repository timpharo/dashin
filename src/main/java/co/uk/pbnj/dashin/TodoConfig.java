package co.uk.pbnj.dashin;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record TodoConfig(
        String url,
        String authToken,
        int shoppingListId,
        int personalListId,
        int workListId,
        int learnListId,
        int inboxListId,
        String tasksPath,
        String projectsPath) {
}
