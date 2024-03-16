package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.TodoConfig;
import co.uk.pbnj.dashin.dto.Project;
import co.uk.pbnj.dashin.dto.Todo;
import co.uk.pbnj.dashin.repository.entity.ProjectResponse;
import co.uk.pbnj.dashin.repository.entity.TaskResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Repository
public class TodoRepository {
    private static final String PROJECT_ID_QUERY_PARAM = "project_id";
    private final String authToken;
    private final String tasksPath;
    private final String projectsPath;
    private final WebClient webClient;

    public TodoRepository(TodoConfig todoConfig) {
        this.authToken = todoConfig.authToken();
        this.tasksPath = todoConfig.tasksPath();
        this.projectsPath = todoConfig.projectsPath();
        this.webClient = WebClient.create(todoConfig.url());
    }

    public List<Todo> getTasks(int listId) {
        Mono<TaskResponse[]> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(tasksPath)
                        .queryParam(PROJECT_ID_QUERY_PARAM, listId)
                        .build())
                .header(AUTHORIZATION, getAuthToken())
                .retrieve()
                .bodyToMono(TaskResponse[].class);

        TaskResponse[] response = responseMono.block();
        return Arrays.stream(response)
                .map(TaskResponse::toTask)
                .toList();
    }

    public List<Project> getLists() {
        Mono<ProjectResponse[]> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(projectsPath)
                        .build())
                .header(AUTHORIZATION, getAuthToken())
                .retrieve()
                .bodyToMono(ProjectResponse[].class);

        ProjectResponse[] response = responseMono.block();
        return Arrays.stream(response)
                .map(ProjectResponse::toProject)
                .toList();
    }

    private String getAuthToken() {
        return "Bearer %s".formatted(authToken);
    }

}

