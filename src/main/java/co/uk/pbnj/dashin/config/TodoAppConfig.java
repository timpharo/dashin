package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.TodoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodoAppConfig {

    @Value("${todo.url}")
    private String url;

    @Value("${todo.authToken}")
    private String authToken;

    @Value("${todo.listId.shopping}")
    private int shoppingProjectId;

    @Value("${todo.listId.personal}")
    private int personalProjectId;

    @Value("${todo.listId.work}")
    private int workProjectId;

    @Value("${todo.listId.learn}")
    private int learnProjectId;

    @Value("${todo.listId.inbox}")
    private int inboxProjectId;

    @Value("${todo.paths.tasks}")
    private String tasksPath;

    @Value("${todo.paths.projects}")
    private String projectsPath;

    @Bean
    TodoConfig todoConfig() {
        return new TodoConfig(
                url,
                authToken,
                shoppingProjectId,
                personalProjectId,
                workProjectId,
                learnProjectId,
                inboxProjectId,
                tasksPath,
                projectsPath);
    }
}
