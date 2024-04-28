package co.uk.pbnj.dashin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TodoConfig {
        private String url;
        private String authToken;
        private int shoppingListId;
        private int personalListId;
        private int workListId;
        private int learnListId;
        private int inboxListId;
        private String tasksPath;
        private String projectsPath;
}
